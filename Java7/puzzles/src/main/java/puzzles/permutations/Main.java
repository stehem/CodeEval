import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main (String[] args) throws IOException {
        File file = new File(args[0]);
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line;
        while ((line = buffer.readLine()) != null && !line.trim().equals("")) {
            line = line.trim();
            List<String> asList = new ArrayList<String>(Arrays.asList(line.split("")));
            asList = asList.subList(1, asList.size());
            Permutator permutator = new Permutator(asList);
            permutator.permute();
            permutator.print();
        }
    }

    private static class Permutator {
        private Map<Integer, String> withIndex = new HashMap<>();
        private List<List<Integer>> permutationsIndex = new ArrayList<>();
        private List<String> permutationsString = new ArrayList<>();
        private int size;

        public Permutator(List<String> chars) {
            for (int i=0;i<chars.size();i++) {
                withIndex.put(i, chars.get(i));
            }
            permutationsIndex.add(new ArrayList<>(withIndex.keySet()));
            size = chars.size();
        }

        public List<List<Integer>> getPermutationsIndex() {
            return permutationsIndex;
        }

        public void permute() {
            while (true) {
                if (size == 1) {
                    break;
                }
                List<Integer> swapped = swapLast2(permutationsIndex.get(permutationsIndex.size() - 1));
                permutationsIndex.add(swapped);
                if (size == 2) {
                    break;
                }
                List<Integer> increment = incrementPrevious(permutationsIndex.get(permutationsIndex.size() - 1));
                if (increment == null) {
                    break;
                }
                permutationsIndex.add(increment);
            }
            setPretty();
        }

        public void print() {
            String string = "";
            int count = 0;
            int last = permutationsString.size();
            for (String perm : permutationsString) {
                count++;
                if (count != last) {
                    string += perm + ",";
                } else {

                    string += perm;
                }
            }
            System.out.println(string);
        }

        private void setPretty() {
            for (List<Integer> indexes : permutationsIndex) {
                permutationsString.add(convertToString(indexes));
            }
            Collections.sort(permutationsString);
        }

        private String convertToString(List<Integer> indexes) {
            String string = "";
            for (int idx : indexes) {
                string += withIndex.get(idx);
            }
            return string;
        }

        private List<Integer> swapLast2(List<Integer> seq) {
            List<Integer> clone = new ArrayList<>(seq);
            int last = clone.get(size - 1);
            int secondToLast = clone.get(size - 2);
            clone.set(size-1, secondToLast);
            clone.set(size - 2, last);
            return clone;
        }

        private int getIncrementedIndex(List<Integer> seq, int position) {
            List<Integer> sub = seq.subList(0,position+1);
            int index = sub.get(sub.size() - 1);
            int incrementedIndex = index + 1;
            while (sub.contains(incrementedIndex)) {
                incrementedIndex = incrementedIndex + 1;
            }
            return incrementedIndex;
        }

        private List<Integer> resetSequence(List<Integer> seq, int position) {
            List<Integer> head = new ArrayList<>();
            for (int i =0;i<=position;i++) {
                head.add(seq.get(i));
            }
            List<Integer> rest = new ArrayList<>();
            for (int i : withIndex.keySet()) {
                if (!head.contains(i)) {
                    rest.add(i);
                }
            }
            head.addAll(rest);
            return head;
        }


        private List<Integer> incrementPrevious(List<Integer> seq) {
            List<Integer> clone = new ArrayList<>(seq);
            int position = size-3;
            int incrementedIndex = getIncrementedIndex(seq, position);
            while (withIndex.get(incrementedIndex) == null) {
                position = position -1;
                if (position == -1) {
                    return null;
                }
                incrementedIndex = getIncrementedIndex(seq, position);
            }
            clone.set(position, incrementedIndex);
            return resetSequence(clone, position);
        }

    }

}


