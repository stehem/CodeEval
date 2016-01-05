package puzzles.prefix;


import java.io.*;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        File file = new File(args[0]);
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line;
        while ((line = buffer.readLine()) != null && !line.trim().equals("")) {
            line = line.trim();
            List<String> asStrings = new ArrayList<>(Arrays.asList(line.split(" ")));
            new PolishMan(asStrings).printResult();
        }
    }


    private static class PolishMan {

        private List<String> prefix;
        private List<String> rewritten;
        private int result;

        public PolishMan(List<String> prefix) {
            this.prefix = prefix;
            this.rewritten = new ArrayList<>(prefix);
            generateExpressions();
            groupExpressions();
            result = evaluateExpression(rewritten.get(0));
        }

        public int getResult() {
            return result;
        }

        public void printResult() {
            System.out.println(result);
        }

        private int calculate() throws ScriptException {
            return evaluateExpression(rewritten.get(0));
        }

        //start from end until we find a non digit
        private void generateExpressions() {
            for (int i = prefix.size() - 1; i >= 0; i--) {
                if (isNumeric(i)) {
                    continue;
                } else {
                    if (isNumeric(i + 1) && isNumeric(i + 2)) {
                        String expr = "(" + prefix.get(i + 1) + prefix.get(i) + prefix.get(i + 2) + ")";
                        rewritten.remove(i);
                        rewritten.remove(i);
                        rewritten.remove(i);
                        rewritten.add(i, expr);
                    }
                }
            }
        }

        private void groupExpressions() {
            for (int i = rewritten.size() - 1; i >= 0; i--) {
                if (isArithmeticOp(rewritten.get(i))) {
                    if (i + 2 <= rewritten.size() && isArithmeticOp(rewritten.get(i))) {
                        String expr = "(" + rewritten.get(i + 1) + rewritten.get(i) + rewritten.get(i + 2) + ")";
                        rewritten.remove(i);
                        rewritten.remove(i);
                        rewritten.remove(i);
                        rewritten.add(i, expr);
                    }
                }
            }
        }


        private boolean isNumeric(int idx) {
            try {
                Integer.parseInt(prefix.get(idx));
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        private boolean isArithmeticOp(String string) {
            return Arrays.asList("*", "/", "+", "-").contains(string);
        }

        private int evaluateExpression(String expr) {
            ScriptEngineManager mgr = new ScriptEngineManager();
            ScriptEngine engine = mgr.getEngineByName("JavaScript");
            try {
                Double asDouble = (Double) engine.eval(expr);
                return asDouble.intValue();
            } catch (ScriptException e) {
                return -1;
            }
        }
    }
}