import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

public class CalculatorTest {
    private Calculator calculator = new Calculator();

    
    @Test
    public void testAddition() {
        List<String> values = Arrays.asList("1", "2", "3");
        double result = calculator.operation("+", values);
        assertEquals(6, result, 0001);
    }


    @Test
    public void testSubtraction() {
        List<String> values = Arrays.asList("10", "3", "2");
        double result = calculator.operation("-", values);
        assertEquals(5, result, 0001);
    }


    @Test
    public void testMultiplication() {
        List<String> values = Arrays.asList("2", "3", "4");
        double result = calculator.operation("*", values);
        assertEquals(24, result, 0001);
    }

    
    @Test
    public void testDivision() {
        List<String> values = Arrays.asList("12", "3", "2");
        double result = calculator.operation("/", values);
        assertEquals(2, result, 0001);
    }

}
