package puzzles.packages;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {


    public static void main(String[] args) throws IOException {
        System.out.flush();
        if (args.length > 0) {
            String path = args[0];
            PackageOptimizer.writeResults(path);
        } else {
            PackageOptimizer.writeResults("/home/user/java/puzzles/target/test-classes/packages.txt");
        }
    }


    private static class PackageOptimizer {

        public static void writeResults(String path) throws IOException {
            PackageOptimizer packageOptimizer = new PackageOptimizer();
            List<Line> lines = packageOptimizer.readLines(path);
            List<String> results = packageOptimizer.getResultsAsStrings(lines);
            packageOptimizer.write(results);
        }

        private List<String> getResultsAsStrings(List<Line> lines) {
            List<String> resultLines = new ArrayList<>();
            for (Line line : lines) {
                List<List<Package>> paths = getPathsWithMaximumWeightForLine(line);
                List<Package> bestCombination = getBestCombination(paths, line.getPackages());
                List<Integer> indices = new ArrayList<>();
                for (Package pack : bestCombination) {
                    indices.add(pack.getIndex());
                }
                Collections.sort(indices);
                String lineString = null;
                if (bestCombination.isEmpty()) {
                    lineString = "-";
                } else {
                    lineString = join(indices, ",");
                }
                resultLines.add(lineString);
            }
            return resultLines;
        }

        private static String join(Collection<?> col, String delim) {
            StringBuilder sb = new StringBuilder();
            Iterator<?> iter = col.iterator();
            if (iter.hasNext())
                sb.append(iter.next().toString());
            while (iter.hasNext()) {
                sb.append(delim);
                sb.append(iter.next().toString());
            }
            return sb.toString();
        }

        private void write(List<String> lines) throws FileNotFoundException, UnsupportedEncodingException {
            for (String line : lines) {
                System.out.println(line);
            }
        }

        private List<Package> getBestCombination(List<List<Package>> optimizedPaths, List<Package> all) {
            List<Package> bestCombination = null;
            for (List<Package> combination : optimizedPaths) {
                if (bestCombination == null) {
                    bestCombination = combination;
                }
                int bestCost = getTotalCost(bestCombination);
                int challengerCost = getTotalCost(combination);
                if (challengerCost > bestCost) {
                    bestCombination = combination;
                }
            }
            return replaceByLighterIfSamePrice(bestCombination, all);
        }

        private List<Package> replaceByLighterIfSamePrice(List<Package> bestCombination, List<Package> all) {
            List<Package> copy = new ArrayList<>();
            for (Package pack : bestCombination) {
                Package lighter = getLighterForSameCost(pack, all);
                if (lighter != null && !bestCombination.contains(lighter)) {
                    copy.add(lighter);
                } else {
                    copy.add(pack);
                }
            }
            return copy;
        }

        private Package getLighterForSameCost(Package pack, List<Package> all) {
            Package best = null;
            for (Package p : all) {
                if (p.getIndex() != pack.getIndex() && p.getCost() == pack.getCost() && p.getWeight() < pack.getWeight()) {
                    best = p;
                }
            }
            return best;
        }

        private static int getTotalCost(List<Package> path) {
            int cost = 0;
            for (Package pack : path) {
                cost = cost + pack.getCost();
            }
            return cost;
        }

        private List<List<Package>> getPathsForLine(Line line) {
            List<List<Package>> allPaths = new ArrayList<>();
            List<Package> packages = line.getPackages();
            int maxIndex = packages.get(packages.size() - 1).getIndex();
            for (Package pack : packages) {
                PackageNode root = new PackageNode(pack);
                root.addChildren(packages, maxIndex);
                PackageTree tree = new PackageTree(root);
                List<Package> newEmpty = new ArrayList<>();
                tree.walk(root, newEmpty);
                allPaths.addAll(tree.getPaths());
            }
            return allPaths;
        }


        private List<List<Package>> getPathsWithMaximumWeightForLine(Line line) {
            List<List<Package>> paths = getPathsForLine(line);
            List<List<Package>> best = new ArrayList<>();

            for (List<Package> path : paths) {
                List<Package> optmizedPath = getMaxCostForMaxWeight(path, line.maxWeight);
                best.add(optmizedPath);
            }
            return best;
        }


        private List<Package> getMaxCostForMaxWeight(List<Package> path, int maxWeight) {
            List<Package> best = new ArrayList<>();
            for (Package pack : path) {
                if (best.isEmpty() && pack.getWeight() <= maxWeight) {
                    best.add(pack);
                }
                double totalWeight = 0;
                for (Package inPath : best) {
                    totalWeight = totalWeight + inPath.getWeight();
                }
                if (totalWeight + pack.getWeight() <= maxWeight && !best.contains(pack)) {
                    best.add(pack);
                }
            }
            return best;
        }

        private List<Line> readLines(String path) throws IOException {
            List<String> linesString = Files.readAllLines(Paths.get(path), Charset.defaultCharset());
            List<Line> lines = new ArrayList<>();
            for (String lineString : linesString) {
                lines.add(parseLine(lineString));
            }
            return lines;
        }

        private Line parseLine(String line) {
            String[] split = line.split(":");
            int maxWeight = Integer.parseInt(split[0].toString().trim());
            String[] packagesString = split[1].trim().split(" ");
            List<Package> packages = new ArrayList<>();
            for (String packageString : packagesString) {
                packageString = packageString.replace("(", "");
                packageString = packageString.replace(")", "");
                String[] packageSplit = packageString.split(",");
                int index = Integer.parseInt(packageSplit[0]);
                double weight = Double.parseDouble(packageSplit[1]);
                String dollar = packageSplit[2].replace("$", "");
                int price = Integer.parseInt(dollar);
                packages.add(new Package(index, weight, price));
            }
            Collections.sort(packages);
            return new Line(maxWeight, packages);
        }

        private static class Line {
            private int maxWeight;
            private List<Package> packages;

            public Line(int maxWeight, List<Package> packages) {
                this.maxWeight = maxWeight;
                this.packages = packages;
            }

            public int getMaxWeight() {
                return maxWeight;
            }

            public List<Package> getPackages() {
                return packages;
            }
        }


        private static class Package implements Comparable {
            private int index;
            private Double weight;
            private int cost;

            public Package(int index, Double weight, int cost) {
                this.index = index;
                this.weight = weight;
                this.cost = cost;
            }

            public int getIndex() {
                return index;
            }

            public Double getWeight() {
                return weight;
            }

            public int getCost() {
                return cost;
            }

            @Override
            public boolean equals(Object o) {
                return o != null && o instanceof Package && ((Package) o).getIndex() == this.getIndex();
            }

            @Override
            public int compareTo(Object o) {
                if (this.getIndex() == ((Package) o).getIndex()) {
                    return 0;
                } else if (this.getIndex() > ((Package) o).getIndex()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        }


        static List<PackageNode> getSiblings(List<Package> orderedPackages, Package pack) {
            List<PackageNode> siblings = new ArrayList<>();
            for (Package p : orderedPackages) {
                if (p.getIndex() > pack.getIndex()) {
                    siblings.add(new PackageNode(p));
                }
            }
            return siblings;
        }


        private static class PackageNode {
            private Package value;
            private List<PackageNode> children = new ArrayList<>();

            public PackageNode(Package value) {
                this.value = value;
            }

            public Package getValue() {
                return value;
            }

            public List<PackageNode> getChildren() {
                return children;
            }

            public void addChildren(List<Package> packages, int maxIndex) {
                if (this.getValue().getIndex() == maxIndex) {
                    return;
                }
                List<PackageNode> siblings = getSiblings(packages, this.getValue());
                this.getChildren().addAll(siblings);
                for (PackageNode child : getChildren()) {
                    child.addChildren(packages, maxIndex);
                }
            }

        }


        private static class PackageTree {
            private PackageNode root;
            private List<List<Package>> paths = new ArrayList<>();

            public PackageTree(PackageNode root) {
                this.root = root;
            }

            public List<List<Package>> getPaths() {
                return paths;
            }


            private void walk(PackageNode node, List<Package> path) {
                path.add(node.getValue());
                if (node.getChildren().isEmpty()) {
                    paths.add(path);
                    return;
                }
                for (PackageNode child : node.getChildren()) {
                    walk(child, new ArrayList<>(path));
                }
            }
        }
    }

}

