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

    private void computeNestingLevel(@NotNull List<Token> tokenList) {
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

    private void replaceExpressionWithResult(@NotNull List<Token> expression, double result, int index) {
        Token resultToken = new Token(Double.toString(result));

        expression.set(index - 1, resultToken);
        expression.subList(index, index + 2).clear();
    }

    private boolean applyOperators(@NotNull List<Token> expression, int strength) {
        for (int i = 0; i < expression.size(); i++) {
            Token token = expression.get(i);

            if (token.isOperator() && token.strength() == strength) {
                final Token left = expression.get(i - 1);
                final Token right = expression.get(i + 1);

                double firstValue = Double.parseDouble(left.getValue());
                double secondValue = Double.parseDouble(right.getValue());

                double result = switch(token.getValue()) {
                    case "*" -> firstValue * secondValue;
                    case "/" -> firstValue / secondValue;
                    case "+" -> firstValue + secondValue;
                    case "-" -> firstValue - secondValue;
                    default -> throw new IllegalArgumentException("Unsupported operator: " + token.getValue());
                };

                replaceExpressionWithResult(expression, result, i);
                return true;
            }
        }
        return false;
    }

    private Token calculateFlatExpression(@NotNull List<Token> expression) {
        removeOuterParentheses(expression);

        while (expression.size() > 1) {
            boolean appliedMultiplication = applyOperators(expression, 2);

            if (!appliedMultiplication)
                applyOperators(expression, 1);
        }
        return expression.getFirst();
    }

    private Range computeMaxNestingLevelPosition(@NotNull List<Token> tokenList, int maxNestingLevel) {
        int start = -1, end = -1;

        for (int i = 0; i < tokenList.size(); i++) {
            Token token = tokenList.get(i);
            String value = token.getValue();

            if (value.equals("(") && token.getNestingLevel() == maxNestingLevel) {
                start = i;
            } else if (value.equals(")")) {
                end = i;
                break;
            }
        }
        return new Range(start, end);
    }

    public Token applyRules(@NotNull List<Token> tokenList) {
        while (tokenList.size() > 1) {
            computeNestingLevel(tokenList);

            int maxNestingLevel = tokenList.stream()
                    .mapToInt(Token::getNestingLevel)
                    .max()
                    .orElse(0);
            Range positions = computeMaxNestingLevelPosition(tokenList, maxNestingLevel);

            int start = positions.start();
            int end = positions.end();

            if (start == -1 || end == -1)
                return calculateFlatExpression(tokenList);

            List<Token> flatExpression = new ArrayList<>(tokenList.subList(start, end + 1));
            Token result = calculateFlatExpression(flatExpression);

            tokenList.set(start, result);
            tokenList.subList(start + 1, end + 1).clear();
        }
        return tokenList.getFirst();
    }

    public void printTokens(@NotNull final List<Token> tokenList) {
        //tokenList.forEach(token -> System.out.println("Value: " + token.getValue() + "\nNesting Level: " + token.getNestingLevel() + "\n"));
        tokenList.forEach(System.out::println);
    }

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter expression to be calculated\n");
        String input = sc.nextLine();

        List<Token> tokenList = calculator.createTokenList(input);
        calculator.printTokens(tokenList);

        final String result = calculator.applyRules(tokenList).getValue();
        System.out.println(result);
    }
}
