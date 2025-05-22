import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Map;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {
    private static final Calculator calculator = new Calculator(true);

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

    @Test
    void multipleDefinitionsTest() {
        final String key = "k";
        final double value = 3.14;

        calculator.define(key, value);
        calculator.define(key, value);
    }

    @Test
    void subtractionTest() {
        final String testExpression = "(25-15)";
        calculator.fillTokenList(testExpression);

        final Token resultToken = calculator.applyRules();
        final double result = Double.parseDouble(resultToken.getValue());

        final double expectedResult = 10.0;

        assertEquals(expectedResult, result);
    }

    @Test
    void divisionTest() {
        final String testExpression = "(100/10)";
        calculator.fillTokenList(testExpression);

        final Token resultToken = calculator.applyRules();
        final double result = Double.parseDouble(resultToken.getValue());

        final double expectedResult = 10.0;
        assertEquals(expectedResult, result);
    }

    @Test
    void inputDefinitionTest() {
        String testInput = "pi\n3.14\nskip\n";

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(testInput.getBytes());
        Scanner scanner = new Scanner(byteArrayInputStream);

        calculator.inputDefinition(scanner);
        Map<String, Double> customDefinitions = calculator.getCustomDefinitions();

        assertEquals(1, customDefinitions.size());
        assertEquals(3.14, customDefinitions.get("pi"));
    }
}