import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Calculator {
    public List<Token> createTokenList(@NotNull String expression) {
        List<Token> tokenList = new ArrayList<>();
        String[] tokens = expression.split("(?<=[-+*/()])|(?=[-+*/()])");

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
    }
}
