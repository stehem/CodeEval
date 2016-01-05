package puzzles.lakes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static void main (String[] args) throws Exception {
        File file = new File(args[0]);
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line;
        while ((line = buffer.readLine()) != null) {
            line = line.trim();
            new Forest(line).printResult();
        }
    }


    private static class Forest {
        private Set<Lake> lakes;
        private Map<Lake, Set<Lake>> adjacents = new HashMap<>();
        private int numberOfLakes;

        public Forest(String line) {
            this.lakes = parseLine(line);
            for (Lake lake : getLakes()) {
                adjacents.put(lake, lake.getAdjacents(this));
            }
            numberOfLakes = mergeLakes(getRoughCount());
        }

        public Set<Lake> getLakes() {
            return lakes;
        }

        public void printResult() {
            System.out.println(numberOfLakes);
        }

        private Set<Lake> parseLine(String line) {
            Set<Lake> lakeList = new HashSet<>();
            List<String> rows = Arrays.asList(line.split("\\|"));
            for (int rowIdx=0;rowIdx<rows.size();rowIdx++) {
                List<String> elements = Arrays.asList(rows.get(rowIdx).split(""));
                List<String> elementsClean = new ArrayList<>();
                for (String elm : elements) {
                    if (!Arrays.asList("", " ").contains(elm)) {
                        elementsClean.add(elm);
                    }
                }
                for (int elementIdx=0;elementIdx<elementsClean.size();elementIdx++) {
                    if (Lake.isLake(elementsClean.get(elementIdx))) {
                        Lake lake = new Lake(elementIdx, rowIdx);
                        lakeList.add(lake);
                    }
                }
            }
            return lakeList;
        }

        private Map<UUID, Set<Lake>> getRoughCount() {
            Map<UUID, Set<Lake>> lakeCount = new HashMap<>();
            for (Map.Entry<Lake, Set<Lake>> adjacentsEntry : adjacents.entrySet()) {
                Set<Lake> keyAndValue = new HashSet<>(adjacentsEntry.getValue());
                keyAndValue.add(adjacentsEntry.getKey());
                if (lakeCount.isEmpty()) {
                    lakeCount.put(UUID.randomUUID(), keyAndValue);
                }
                UUID intersect = intersectWith(lakeCount, keyAndValue);
                if (intersect != null) {
                    lakeCount.get(intersect).addAll(keyAndValue);
                } else {
                    lakeCount.put(UUID.randomUUID(), keyAndValue);
                }

            }
            return lakeCount;
        }

        private int mergeLakes(Map<UUID, Set<Lake>> lakeCount) {
            Map<UUID, Set<Lake>> result = new ConcurrentHashMap<>(lakeCount);
            List<Integer> sizes = new ArrayList<>();
            sizes.add(result.size());
            while (sizes.size() <= 1 || !sizes.get(sizes.size()-1).equals(sizes.get(sizes.size()-2))) {
                for (Map.Entry<UUID, Set<Lake>> entry : result.entrySet()) {
                    for (Map.Entry<UUID, Set<Lake>> entryy : result.entrySet()) {
                        if (entryy.getKey().equals(entry.getKey())) {
                            continue;
                        }
                        Set<Lake> clone = new HashSet<>(entryy.getValue());
                        clone.retainAll(entry.getValue());
                        if (!clone.isEmpty()) {
                            Set<Lake> consolidated = new HashSet<>(entryy.getValue());
                            consolidated.addAll(entry.getValue());
                            result.put(UUID.randomUUID(), consolidated);
                            result.remove(entry.getKey());
                            result.remove(entryy.getKey());
                        }
                    }
                }
                sizes.add(result.size());
            }
            return result.size();
        }

        private UUID intersectWith(Map<UUID, Set<Lake>> lakeCount, Set<Lake> keyAndValue) {
            for (Map.Entry<UUID, Set<Lake>> lakeCountEntry : lakeCount.entrySet()) {
                Set<Lake> clone = new HashSet<>(lakeCountEntry.getValue());
                clone.retainAll(keyAndValue);
                if (!clone.isEmpty()) {
                    return lakeCountEntry.getKey();
                }
            }
            return null;
        }

    }

    private static class Lake {
        private int x;
        private int y;

        public Lake(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Lake lake = (Lake) o;
            return Objects.equals(x, lake.x) &&
                    Objects.equals(y, lake.y);
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        public Set<Lake> getAdjacents(Forest forest) {
            Set<Lake> adjacents = new HashSet<>();
            for (Lake lake : forest.getLakes()) {
                if (this.equals(lake)) {
                    continue;
                }
                if (this.isAdjacent(lake)) {
                    adjacents.add(lake);
                }
            }
            return adjacents;
        }

        public static boolean isLake(String element) {
            return element.equals("o");
        }

        private boolean isAdjacent(Lake lake) {
            List<Integer> xRange = Arrays.asList(this.getX()-1, this.getX(), this.getX()+1);
            List<Integer> yRange = Arrays.asList(this.getY()-1, this.getY(), this.getY()+1);
            return xRange.contains(lake.getX()) && yRange.contains(lake.getY());
        }

    }

}
