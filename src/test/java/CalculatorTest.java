import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {
    private static final Calculator calculator = new Calculator(false);

    @Test
    void expressionWithDefinitionTest() {
        final String firstKey = "pi";
        final double firstValue = 3.1415;
        calculator.define(firstKey, firstValue);

        final String secondKey = "r";
        final double secondValue = 4;
        calculator.define(secondKey, secondValue);

        final String testExpression = "2*pi*r";
        calculator.fillTokenList(testExpression);

        Token resultToken = calculator.applyRules();

        final double result = Double.parseDouble(resultToken.getValue());
        final double expectedResult = 25.132;
        final double floatingPointTolerance = 0.001;

        assertEquals(expectedResult, result, floatingPointTolerance);
    }

    @Test
    void expressionWithSimilarKeysTest() {
        final String firstKey = "a";
        final double firstValue = 2;
        calculator.define(firstKey, firstValue);

        final String secondKey = "b";
        final double secondValue = 3;
        calculator.define(secondKey, secondValue);

        final String thirdKey = "bb";
        final double thirdValue = 5;
        calculator.define(thirdKey, thirdValue);

        final String fourthKey = "ab";
        final double fourthValue = 7;
        calculator.define(fourthKey, fourthValue);

        final String testExpression = "a+bb*b+ab";
        calculator.fillTokenList(testExpression);

        Token resultToken = calculator.applyRules();

        final double result = Double.parseDouble(resultToken.getValue());
        final double expectedResult = 24;

        assertEquals(expectedResult, result);
    }

    @Test
    void expressionWithParenthesesTest() {
        final String firstKey = "a";
        final double firstValue = 2;
        calculator.define(firstKey, firstValue);

        final String secondKey = "b";
        final double secondValue = 3;
        calculator.define(secondKey, secondValue);

        final String thirdKey = "bb";
        final double thirdValue = 5;
        calculator.define(thirdKey, thirdValue);

        final String fourthKey = "ab";
        final double fourthValue = 7;
        calculator.define(fourthKey, fourthValue);

        final String testExpression = "(a+bb)*((b+ab))";
        calculator.fillTokenList(testExpression);

        Token resultToken = calculator.applyRules();

        final double result = Double.parseDouble(resultToken.getValue());
        final double expectedResult = 70;

        assertEquals(expectedResult, result);
    }
}