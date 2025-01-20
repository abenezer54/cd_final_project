import java.util.*;

public class LL1Parser {
    private Map<String, Map<String, String>> parsingTable;
    private Stack<String> stack;
    private List<String> tokens;
    private int index;

    public LL1Parser() {
        parsingTable = new HashMap<>();
        stack = new Stack<>();
        initializeParsingTable();
    }

    private void initializeParsingTable() {
        parsingTable.put("E", new HashMap<>());
        parsingTable.put("E'", new HashMap<>());
        parsingTable.put("T", new HashMap<>());
        parsingTable.put("T'", new HashMap<>());
        parsingTable.put("F", new HashMap<>());

        parsingTable.get("E").put("(", "TE'");
        parsingTable.get("E").put("id", "TE'");
        parsingTable.get("E'").put("+", "+TE'");
        parsingTable.get("E'").put(")", "ε");
        parsingTable.get("E'").put("$", "ε");
        parsingTable.get("T").put("(", "FT'");
        parsingTable.get("T").put("id", "FT'");
        parsingTable.get("T'").put("+", "ε");
        parsingTable.get("T'").put("*", "*FT'");
        parsingTable.get("T'").put(")", "ε");
        parsingTable.get("T'").put("$", "ε");
        parsingTable.get("F").put("(", "(E)");
        parsingTable.get("F").put("id", "id");
    }

    public boolean parse(String input) {
        this.tokens = tokenize(input + "$");
        this.index = 0;
        stack.clear();
        stack.push("E");

        while (!stack.isEmpty()) {
            String top = stack.pop();
            System.out.println("Top of stack: " + top);
            if (isNonTerminal(top)) {
                if (index >= tokens.size()) {
                    return false;
                }
                String symbol = tokens.get(index);
                System.out.println("Current symbol: " + symbol);
                String production = parsingTable.get(top).get(symbol);
                System.out.println("Production: " + production);
                if (production == null) {
                    return false;
                }
                if (!production.equals("ε")) {
                    List<String> productionTokens = tokenizeProduction(production);
                    for (int i = productionTokens.size() - 1; i >= 0; i--) {
                        stack.push(productionTokens.get(i));
                    }
                }
            } else {
                if (index >= tokens.size() || !top.equals(tokens.get(index))) {
                    return false;
                }
                System.out.println("Matched terminal: " + top);
                index++;
            }
        }
        return index == tokens.size() - 1;
    }

    private boolean isNonTerminal(String symbol) {
        return parsingTable.containsKey(symbol);
    }

    private List<String> tokenize(String input) {
        List<String> tokens = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == 'i' && i + 1 < input.length() && input.charAt(i + 1) == 'd') {
                tokens.add("id");
                i++;
            } else if (c == '+' || c == '*' || c == '(' || c == ')' || c == '$') {
                tokens.add(String.valueOf(c));
            }
        }
        return tokens;
    }

    private List<String> tokenizeProduction(String production) {
        List<String> tokens = new ArrayList<>();
        for (int i = 0; i < production.length(); i++) {
            char c = production.charAt(i);
            if (c == 'i' && i + 1 < production.length() && production.charAt(i + 1) == 'd') {
                tokens.add("id");
                i++;
            } else if (c == 'E' && i + 1 < production.length() && production.charAt(i + 1) == '\'') {
                tokens.add("E'");
                i++;
            } else if (c == 'T' && i + 1 < production.length() && production.charAt(i + 1) == '\'') {
                tokens.add("T'");
                i++;
            } else {
                tokens.add(String.valueOf(c));
            }
        }
        return tokens;
    }

    public static void main(String[] args) {
        LL1Parser parser = new LL1Parser();
        System.out.println(parser.parse("id")); // true
        System.out.println(parser.parse("id+id")); // true
        System.out.println(parser.parse("id+id*id")); // true
        System.out.println(parser.parse("id+*id")); // false
    }
}