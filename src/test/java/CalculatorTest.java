import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Map;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {
    private Calculator calculator;

    @BeforeEach
    void init() { calculator = new Calculator(true); }

    @Test
    void expressionWithDefinitionTest() {
        calculator.define("pi", 3.1415);
        calculator.define("r", 4);

        String testExpression = "2*pi*r";
        calculator.fillTokenList(testExpression);

        Token resultToken = calculator.applyRules();
        double result = Double.parseDouble(resultToken.getValue());

        double expectedResult = 25.132;
        double floatingPointTolerance = 0.001;

        assertEquals(expectedResult, result, floatingPointTolerance);
    }

    @Test
    void expressionWithSimilarKeysTest() {
        calculator.define("a", 2);
        calculator.define("b", 3);
        calculator.define("bb", 5);
        calculator.define("ab", 7);

        String testExpression = "a+bb*b+ab";
        calculator.fillTokenList(testExpression);

        Token resultToken = calculator.applyRules();

        double result = Double.parseDouble(resultToken.getValue());
        double expectedResult = 24;

        assertEquals(expectedResult, result);
    }

    @Test
    void expressionWithParenthesesTest() {
        calculator.define("a", 2);
        calculator.define("b", 3);
        calculator.define("bb", 5);
        calculator.define("ab", 7);

        String testExpression = "(a+bb)*((b+ab))";
        calculator.fillTokenList(testExpression);

        Token resultToken = calculator.applyRules();

        double result = Double.parseDouble(resultToken.getValue());
        double expectedResult = 70;

        assertEquals(expectedResult, result);
    }

    @Test
    void multipleDefinitionsTest() {
        calculator.define("k", 3.14);
        calculator.define("k", 3.14);

        Map<String, Double> customDefinitions = calculator.getCustomDefinitions();
        assertEquals(1, customDefinitions.size());
    }

    @Test
    void subtractionTest() {
        String testExpression = "(25-15)";
        calculator.fillTokenList(testExpression);

        Token resultToken = calculator.applyRules();
        double result = Double.parseDouble(resultToken.getValue());

        assertEquals(10.0, result);
    }

    @Test
    void divisionTest() {
        String testExpression = "(100/10)";
        calculator.fillTokenList(testExpression);

        Token resultToken = calculator.applyRules();
        double result = Double.parseDouble(resultToken.getValue());

        assertEquals(10.0, result);
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