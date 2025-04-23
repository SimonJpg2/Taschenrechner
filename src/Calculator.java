import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Calculator {
    public List<Token> createTokenList(@NotNull String expression) {
        List<Token> tokenList = new ArrayList<>();
        final String[] values = expression.split("(?<=[-+*/()])|(?=[-+*/()])");

        Arrays.stream(values).forEach(
                value -> tokenList.add(new Token(value))
        );
        return tokenList;
    }

    public void computeNestingLevel(@NotNull List<Token> tokenList) {
        int nestingLevel = 0;

        for (Token token : tokenList) {
            final String value = token.getValue();

            if (value.equals("("))
                nestingLevel++;

            if (value.equals(")"))
                nestingLevel--;

            token.setNestingLevel(nestingLevel);
        }
    }

    private void removeOuterParentheses(@NotNull List<Token> expression) {
        final String firstValue = expression.getFirst().getValue();
        final String lastValue = expression.getLast().getValue();

        if (!expression.isEmpty() && firstValue.equals("("))
            expression.removeFirst();

        if (!expression.isEmpty() && lastValue.equals(")"))
            expression.removeLast();
    }

    public Token calculateFlatExpression(@NotNull List<Token> expression) {
        expression.forEach(t -> System.out.println("Expression: " + t.getValue()));
        removeOuterParentheses(expression);

        while (expression.size() > 1) {
            for (int i = 0; i < expression.size(); i++) {
                if (expression.get(i).isOperator() && expression.get(i).strength() == 2) {
                    double left = Double.parseDouble(expression.get(i - 1).getValue());
                    double right = Double.parseDouble(expression.get(i + 1).getValue());
                    double result = expression.get(i).getValue().equals("*") ? left * right : left / right;

                    Token resultToken = new Token(Double.toString(result));
                    expression.set(i - 1, resultToken);
                    expression.subList(i, i + 2).clear();

                    expression.forEach(t -> System.out.println("AfterCalculation: " + t.getValue()));
                    break;
                }
            }

            for (int i = 0; i < expression.size(); i++) {
                if (expression.get(i).isOperator() && expression.get(i).strength() == 1) {
                    double left = Double.parseDouble(expression.get(i - 1).getValue());
                    double right = Double.parseDouble(expression.get(i + 1).getValue());
                    double result = expression.get(i).getValue().equals("+") ? left + right : left - right;

                    Token resultToken = new Token(Double.toString(result));
                    expression.set(i - 1, resultToken);
                    expression.subList(i, i + 2).clear();

                    expression.forEach(t -> System.out.println("AfterCalculation: " + t.getValue()));
                    break;
                }
            }
            expression.forEach(t -> System.out.println("AfterIteration: " + t.getValue()));
        }
        return expression.getFirst();
    }

    public Token applyRules(@NotNull List<Token> tokenList) {
        while (tokenList.size() > 1) {
            computeNestingLevel(tokenList);

            int maxNestingLevel = tokenList.stream()
                    .mapToInt(Token::getNestingLevel)
                    .max()
                    .orElse(0);

            int start = -1, end = -1;
            for (int i = 0; i < tokenList.size(); i++) {
                Token t = tokenList.get(i);
                System.out.println("ApplyRules: " + t.getValue() + " nesting level: " + t.getNestingLevel());
                if (t.getValue().equals("(") && t.getNestingLevel() == maxNestingLevel) {
                    start = i;
                } else if (t.getValue().equals(")")) {
                    end = i;
                    break;
                }
            }

            System.out.println("First Index: " + start);
            System.out.println("Last Index: " + end);

            if (start == -1 || end == -1)
                return calculateFlatExpression(tokenList);

            List<Token> expression = new ArrayList<>(tokenList.subList(start, end + 1));
            Token result = calculateFlatExpression(expression);

            tokenList.set(start, result);
            tokenList.subList(start + 1, end + 1).clear(); // Changes position of indices!
        }
        return tokenList.getFirst();
    }

    public void printTokens(@NotNull final List<Token> tokenList) {
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

        final String result = calculator.applyRules(tokenList).getValue();
        System.out.println(result);
    }
}
