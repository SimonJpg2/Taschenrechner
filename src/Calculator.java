import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Calculator {
    public List<Token> createTokenList(@NotNull String expression) {
        List<Token> tokenList = new ArrayList<>();
        String[] tokens = expression.split("(?<=[-+*/()])|(?=[-+*/()])"); // Regex teilt alles links und rechts von Zahlen auf

        Arrays.stream(tokens).forEach(
                value -> tokenList.add(new Token(value))
        );

        return tokenList;
    }

    public void computeNestingLevel(@NotNull List<Token> tokenList) {
        int nestingLevel = 0;

        for (Token token : tokenList) {
            if (token.getValue().equals("(")) {
                nestingLevel++;
            } else if (token.getValue().equals(")")) {
                nestingLevel--;
            }
            token.setNestingLevel(nestingLevel);
        }
    }

    /*public List<Integer> computeIndices(@NotNull List<Token> tokenList) {
        List<Integer> indices = new ArrayList<>();

        int maxNestingLevel = Collections.max(
                tokenList, Comparator.comparing(Token::getNestingLevel)
        ).getNestingLevel();

        for (int i = 0; i < tokenList.size(); i++)
            if (tokenList.get(i).getNestingLevel() == maxNestingLevel)
                indices.add(i);
        return indices;
    }*/



    public Token applyRules(List<Token> tokenList) {
        // TODO: zwei Rekursive Funktionen: Eine zum Berechnen der Klammern, eine weitere zum Berechnen mehrerer Ausdrücke in einer Klammer

        int maxNestingLevel = tokenList.stream()
                .mapToInt(Token::getNestingLevel)
                .max()
                .orElse(0);

        int start = -1, end = -1;
        for (int i = 0; i < tokenList.size(); i++) {
            Token t = tokenList.get(i);
            if (t.getValue().equals("(") && t.getNestingLevel() == maxNestingLevel) {
                start = i;
            } else if (t.getValue().equals(")")) {
                end = i;
                break;
            }
        }

        System.out.println("First Index: " + start);
        System.out.println("Last Index: " + end);

        return null;
    }

    public void printTokens(@NotNull List<Token> tokenList) {
        tokenList.forEach(token -> System.out.println("Value: " + token.getValue() + "\nNesting Level: " + token.getNestingLevel() + "\n"));
    }

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter expression to be calculated\n");
        String input = sc.nextLine();

        List<Token> tokenList = calculator.createTokenList(input);

        calculator.computeNestingLevel(tokenList);
        calculator.printTokens(tokenList);

        System.out.println("Test:");
        System.out.println(calculator.applyRules(tokenList).getValue());
    }
}
