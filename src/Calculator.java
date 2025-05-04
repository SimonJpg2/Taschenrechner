import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Calculator {
    private final int DEFAULT_POSITION = -1;

    private final Map<String, Double> customExpressions = new HashMap<>();
    private final List<Token> tokenList = new ArrayList<>();

    public void define(final String key, final double value) {
        if (customExpressions.containsKey(key)) {
            System.out.printf("Definition for \"%s\" already exists.", key);
            return;
        }

        customExpressions.put(key, value);
    }

    public void fillTokenList(@NotNull String input) {
        final String[] values = input.split("(?<=[-+*/()])|(?=[-+*/()])");

        Arrays.stream(values).forEach(
                value -> this.tokenList.add(new Token(value))
        );
    }

    private void computeNestingLevel() {
        int nestingLevel = 0;

        for (Token token : this.tokenList) {
            final String value = token.getValue();

            if (value.equals("("))
                nestingLevel++;

            if (value.equals(")"))
                nestingLevel--;

            token.setNestingLevel(nestingLevel);
        }
    }

    private void removeOuterParentheses(@NotNull List<Token> flatExpression) {
        final String firstValue = flatExpression.getFirst().getValue();
        final String lastValue = flatExpression.getLast().getValue();

        if (!flatExpression.isEmpty() && firstValue.equals("("))
            flatExpression.removeFirst();

        if (!flatExpression.isEmpty() && lastValue.equals(")"))
            flatExpression.removeLast();
    }

    private void replaceExpressionWithResult(@NotNull List<Token> flatExpression, double result, int index) {
        Token resultToken = new Token(Double.toString(result));

        /*
        *  x   +   y  | flatExpression
        * i-1  i  i+1 | indices
        */

        flatExpression.set(index - 1, resultToken);
        flatExpression.subList(index, index + 2).clear();
    }

    private Token replaceDefinitionWithValue(Token token) {
        final String value = token.getValue();

        if (customExpressions.containsKey(value))
            return new Token(customExpressions.get(value).toString(), token.getNestingLevel());

        return token;
    }

    private boolean applyOperators(@NotNull List<Token> flatExpression, int strength) {
        for (int i = 0; i < flatExpression.size(); i++) {
            Token operator = flatExpression.get(i);

            if (operator.isOperator() && operator.strength() == strength) {
                Token left = flatExpression.get(i - 1);
                Token right = flatExpression.get(i + 1);

                left = replaceDefinitionWithValue(left);
                right = replaceDefinitionWithValue(right);

                double firstValue = Double.parseDouble(left.getValue());
                double secondValue = Double.parseDouble(right.getValue());

                double result = switch(operator.getValue()) {
                    case "*" -> firstValue * secondValue;
                    case "/" -> firstValue / secondValue;
                    case "+" -> firstValue + secondValue;
                    case "-" -> firstValue - secondValue;
                    default -> throw new IllegalArgumentException("Unsupported operator: " + operator.getValue());
                };

                replaceExpressionWithResult(flatExpression, result, i);
                return true;
            }
        }
        return false;
    }

    private Token calculateFlatExpression(@NotNull List<Token> flatExpression) {
        removeOuterParentheses(flatExpression);

        while (flatExpression.size() > 1) {
            boolean appliedStrength2Operation = applyOperators(flatExpression, 2);

            if (!appliedStrength2Operation)
                applyOperators(flatExpression, 1);
        }
        return flatExpression.getFirst();
    }

    private Range computeFlatExpressionPosition(int maxNestingLevel) {
        int start = DEFAULT_POSITION, end = DEFAULT_POSITION;

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

    public Token applyRules() {
        while (tokenList.size() > 1) {
            computeNestingLevel();

            int maxNestingLevel = tokenList.stream()
                    .mapToInt(Token::getNestingLevel)
                    .max()
                    .orElse(0);

            Range positions = computeFlatExpressionPosition(maxNestingLevel);

            int start = positions.start();
            int end = positions.end();

            if (start == DEFAULT_POSITION || end == DEFAULT_POSITION)
                return calculateFlatExpression(tokenList);

            List<Token> flatExpression = new ArrayList<>(tokenList.subList(start, end + 1));
            Token result = calculateFlatExpression(flatExpression);

            tokenList.set(start, result);
            tokenList.subList(start + 1, end + 1).clear();
        }
        return tokenList.getFirst();
    }

    public void printTokens() {
        this.tokenList.forEach(System.out::println);
    }

    public void inputDefinition(@NotNull final Scanner sc) {
        String definition;
        double value;

        while (true) {
            System.out.println("Enter a definition for a specific number. If you want to to skip this step, enter \"skip\"");
            definition = sc.nextLine();

            if (definition.equalsIgnoreCase("skip"))
                return;

            System.out.println("Enter a value for your specified definition: ");
            value = Double.parseDouble(sc.nextLine());
            this.define(definition, value);
        }
    }

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        Scanner sc = new Scanner(System.in);

        calculator.inputDefinition(sc);

        System.out.println("Enter expression to be calculated\n");
        String input = sc.nextLine();

        calculator.fillTokenList(input);
        calculator.printTokens();

        final String result = calculator.applyRules().getValue();
        System.out.println(result);
    }
}
