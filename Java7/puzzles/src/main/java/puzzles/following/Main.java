package puzzles.following;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

public class Main {
    public static void main(String[] args) throws Exception {
        File file = new File(args[0]);
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line;
        while ((line = buffer.readLine()) != null) {
            line = line.trim();
            new FollowingInteger(line).printResult();
        }
    }

    private static class FollowingInteger {
        private List<Integer> number = new ArrayList<>();
        private List<Integer> sorted;
        private List<Integer> following;

        public FollowingInteger(String line) {
            List<String> nbArray = asList(line.split(""));
            nbArray = nbArray.subList(1, nbArray.size());
            for (String nb : nbArray) {
                number.add(Integer.parseInt(nb));
            }
            sorted = new ArrayList<>(number);
            Collections.sort(sorted);
            findFollowing();
        }

        public void printResult() {
            String result = "";
            for (Integer f : following) {
                result += Integer.toString(f);
            }
            System.out.println(result);
        }

        private void findFollowing() {
            if (cannotSwap()) {
                following = addZero(sorted);
                return;
            }
            List<List<Integer>> result = new ArrayList<>();
            for (int i=number.size()-1;i>=0;i--) {
                Integer idxOfFirstBigger = getIndexOfFirstSmaller(i);
                if (idxOfFirstBigger != null) {
                    List<Integer> candidate = swap(i, idxOfFirstBigger, number);
                    candidate  = reorderRest(candidate, idxOfFirstBigger);
                    result.add(candidate);
                }
            }
            int sum = Integer.MAX_VALUE;
            List<Integer> foll = null;
            for (List<Integer> r : result) {
                String sumString = "";
                for (Integer i : r) {
                    sumString+=i;
                }
                int res = Integer.parseInt(sumString);
                if (res < sum) {
                    sum = res;
                    foll = r;
                }
            }
            if (foll != null) {
                following = foll;
                return;
            }
            following = addZero(number);
        }

        private List<Integer> reorderRest(List<Integer> numbers, Integer idx) {
            List<Integer> head = numbers.subList(0, idx + 1);
            List<Integer> rest = numbers.subList(idx + 1, numbers.size());
            Collections.sort(rest);
            List<Integer> result = new ArrayList<>();
            result.addAll(head);
            result.addAll(rest);
            return result;

        }

        private List<Integer> addZero(List<Integer> numbers) {
            List<Integer> clone = new ArrayList<>(numbers);
            clone.add(1, 0);
            return clone;
        }

        private List<Integer> swap(int idx, int idy, List<Integer> numbers) {
            List<Integer> clone = new ArrayList<>(numbers);
            Integer i = clone.get(idx);
            Integer j = clone.get(idy);
            clone.set(idx, j);
            clone.set(idy, i);
            return clone;
        }


        private Integer getIndexOfFirstSmaller(Integer idx) {
            for (int i=idx;i>=0;i--) {
                if (number.get(i) < number.get(idx)) {
                    return i;
                }
            }
            return null;
        }

        private boolean cannotSwap() {
            if (number.get(number.size()-1) == 0) {
                return false;
            }
            List<Integer> sortedDesc = new ArrayList<>(sorted);
            Collections.reverse(sortedDesc);
            return sortedDesc.equals(number);
        }

    }

}
