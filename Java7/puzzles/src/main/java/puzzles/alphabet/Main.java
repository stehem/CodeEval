package puzzles.alphabet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        File file = new File(args[0]);
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line;
        while ((line = buffer.readLine()) != null) {
            line = line.trim();
            new Line(line).printResult();
        }
    }


    private static class Line {
        private int blockQty;
        private List<String> word = new ArrayList<>();
        private List<List<String>> blocks = new ArrayList<>();
        private List<List<String>> blocksCopy = new ArrayList<>();
        private Map<String, String> result = new TreeMap<>();
        private boolean canSpell;

        public Line(String line) {
            List<String> split = Arrays.asList(line.split("\\|"));
            String qty = split.get(0).trim();
            blockQty = Integer.parseInt(qty);
            String wordAsString = split.get(1).trim();
            word = splitByEmpty(wordAsString);
            List<String> blockss = splitBySpace(split.get(2));
            for (String block : blockss) {
                blocks.add(splitByEmpty(block));
            }
            blocksCopy = new ArrayList<>(blocks);
            generateResults();
            canSpellWord();
        }

        public void printResult() {
            System.out.println(canSpell ? "True" : "False");
        }


        private void generateResults() {
            for (String wordLetter : word) {
                List<String> bestBlock = getBestBlockForLetter(wordLetter);
                if (bestBlock != null) {
                    result.put(bestBlock.toString() + UUID.randomUUID().toString().split("-")[1], wordLetter);
                    blocksCopy.remove(bestBlock);
                }
            }
        }


        private List<String> getBestBlockForLetter(String letter) {
            List<List<String>> blocksHavingLetter = getBlocksContainingLetter(letter);
            Map<List<String>, Integer> scores = getScores(letter, blocksHavingLetter);
            return getBlockHavingBestScore(scores);
        }


        private Map<List<String>, Integer> getScores(String letter, List<List<String>> blocksHavingLetter) {
            Map<List<String>, Integer> scores = new HashMap<>();
            for (List<String> block : blocksHavingLetter) {
                int score = 0;
                List<String> repeat = new ArrayList<>();
                for (String blockLetter : block) {
                    if (!word.contains(blockLetter)) {
                        score += 3;
                        continue;
                    }
                    if (repeat.contains(blockLetter)) {
                        score += 3;
                        continue;
                    }
                    int neededByWord = neededByWord(blockLetter);
                    int available = getBlocksContainingLetter(blockLetter).size();
                    if (blockLetter.equals(letter)) {
                        if (neededByWord == available) {
                            score += 1000;
                        } else if (neededByWord < available) {
                            score += available - neededByWord;
                        }
                    } else {
                        if (neededByWord == available) {
                            score -= 1000;
                        } else if (neededByWord < available) {
                            score += available - neededByWord;
                        }
                    }
                    repeat.add(blockLetter);
                }
                scores.put(block, score);
            }
            return scores;
        }

        private List<String> getBlockHavingBestScore(Map<List<String>, Integer> scores) {
            List<Map.Entry<List<String>, Integer>> entries = new ArrayList<>(scores.entrySet());
            if (entries.isEmpty()) {
                return null;
            }
            Collections.sort(entries, new Comparator<Map.Entry<List<String>, Integer>>() {
                public int compare(Map.Entry<List<String>, Integer> o1, Map.Entry<List<String>, Integer> o2) {
                    return (o1.getValue()).compareTo(o2.getValue());
                }
            });
            return entries.get(entries.size() - 1).getKey();
        }


        private int neededByWord(String letter) {
            int needed = 0;
            int already = 0;
            for (Map.Entry<String, String> resultEntry : result.entrySet()) {
                if (resultEntry.getValue().equals(letter)) {
                    already++;
                }
            }
            for (String wordLetter : word) {
                if (wordLetter.equals(letter)) {
                    needed++;
                }
            }
            return needed - already;
        }


        private List<List<String>> getBlocksContainingLetter(String letter) {
            List<List<String>> result = new ArrayList<>();
            for (List<String> block : blocksCopy) {
                if (block.contains(letter)) {
                    result.add(block);
                }
            }
            return result;
        }


        private void canSpellWord() {
            List<String> wordCopy = new ArrayList<>(word);
            Collections.sort(wordCopy);
            List<String> resultData = new ArrayList<>(result.values());
            Collections.sort(resultData);
            canSpell = wordCopy.equals(resultData);
        }

        private List<String> splitBySpace(String raw) {
            List<String> clean = new ArrayList<>();
            List<String> split = Arrays.asList(raw.split(" "));
            for (String s : split) {
                if (!Arrays.asList("", " ").contains(s)) {
                    clean.add(s.trim());
                }
            }
            return clean;
        }

        private List<String> splitByEmpty(String raw) {
            List<String> rawsplit = new ArrayList<>(Arrays.asList(raw.split("")));
            rawsplit.remove(0);
            return rawsplit;
        }

    }
}
