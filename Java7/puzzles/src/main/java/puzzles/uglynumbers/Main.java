import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main (String[] args) throws IOException {
        File file = new File(args[0]);
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String input;
        while ((input = buffer.readLine()) != null && !input.trim().equals("")) {
            input = input.trim();
            List<String> numbersAsString = Arrays.asList(input.split(""));
            numbersAsString = numbersAsString.subList(1, numbersAsString.size());
            List<Long> numbers = new ArrayList<>();
            for (String nb : numbersAsString) {
                numbers.add(Long.parseLong(nb));
            }

            List<Digit> line = Line.generateChildren(new Digit(-1,-1,true), numbers, true);
            List<Node> root = new ArrayList<>();
            for (Digit digit : line) {
                root.add(new Node(digit));
            }
            root.get(0).generateChildren(root, numbers);

            Tree tree = new Tree(root, numbers);
            tree.generatePaths(root);
            tree.calculateUglies();
            tree.printUglies();

        }
    }

    private static class Line {

        public static List<Digit> generateChildren(Digit digit, List<Long> line, boolean firstLine) {
            List<Digit> children = new ArrayList<>();
            for (int j=digit.getEndIdx()+1;j<line.size();j++) {
                Digit digitPos = new Digit(digit.getEndIdx()+1, j, true);
                Digit digitNeg = new Digit(digit.getEndIdx()+1, j, false);
                children.add(digitPos);
                if (!firstLine) {
                    children.add(digitNeg);
                }
            }
            return children;
        }

        public static Long toInt(List<Long> list) {
            String empty = "";
            for (Long i : list) {
                empty+=String.valueOf(i);
            }
            return Long.parseLong(empty);
        }
    }

    private static class Digit {
        private int startIdx;
        private int endIdx;
        private boolean positive;

        public Digit(int startIdx, int endIdx, boolean positive) {
            this.startIdx = startIdx;
            this.endIdx = endIdx;
            this.positive = positive;
        }

        public int getStartIdx() {
            return startIdx;
        }

        public int getEndIdx() {
            return endIdx;
        }
    }

    private static class Node {
        private Digit value;
        private List<Node> children = new ArrayList<>();

        public Node(Digit value) {
            this.value = value;
        }

        public List<Node> getChildren() {
            return children;
        }

        public Digit getValue() {
            return value;
        }

        public void generateChildren(List<Node> nodes, List<Long> line) {
            for (Node node : nodes) {
                List<Digit> digits = Line.generateChildren(node.getValue(), line, false);
                for (Digit digit : digits) {
                    node.getChildren().add(new Node(digit));
                }
                generateChildren(node.getChildren(), line);
            }
        }

    }

    private static class Tree {
        private List<Long> line;
        private List<Node> root;
        private List<List<Long>> paths = new ArrayList<>();
        private int numberOfUglies = 0;

        public Tree(List<Node> root, List<Long> line) {
            this.root = root;
            this.line = line;
        }

        public void generatePaths(List<Node> roots) {
            for (Node node : roots) {
                generatePathsForNode(null, node);
            }
        }

        public void calculateUglies() {
            for (List<Long> path : paths) {
                if (isUgly(sumList(path))) {
                    numberOfUglies++;
                }
            }
        }

        public void printUglies() {
            System.out.println(numberOfUglies);
        }

        private void generatePathsForNode(List<Digit> path, Node rootNode) {
            if (path == null) {
                path = new ArrayList<>();
            }
            path.add(rootNode.getValue());
            if (rootNode.getChildren().isEmpty()) {
                paths.add(toPrettyPath(path));
                return;
            }
            for (Node child : rootNode.getChildren()) {
                generatePathsForNode(new ArrayList<>(path), child);
            }
        }


        private List<Long> toPrettyPath(List<Digit> digits) {
            List<Long> path = new ArrayList<>();
            for (Digit digit : digits) {
                List<Long> sub = line.subList(digit.getStartIdx(), digit.getEndIdx() + 1);
                Long asInt = Line.toInt(sub) * (digit.positive ? 1 : -1);
                path.add(asInt);
            }
            return path;
        }

        private int sumList(List<Long> list) {
            int sum = 0;
            for (Long nb : list) {
                sum+=nb;
            }
            return sum;
        }

        private boolean isUgly(int number) {
            return  number == 0 || number % 2 == 0 || number % 3 == 0 || number % 5 == 0 || number % 7 == 0;
        }

    }


}


