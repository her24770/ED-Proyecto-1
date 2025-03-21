import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
public class PredicateTest {

    /**
     * TESTS PARA LA FUNCION isNumber
     */

    
    @Test
    public void testIsNumberWithInteger() {
        assertEquals(Predicate.isNumber("123"), true);
    }


    @Test
    public void testIsNumberWithNegativeInteger() {
        assertEquals(Predicate.isNumber("-123"), true);
    }

    
    @Test
    public void testIsNumberWithDecimal() {
        assertEquals(Predicate.isNumber("123.45"), true);
    }

    
    @Test
    public void testIsNumberWithNegativeDecimal() {
        assertEquals(Predicate.isNumber("-123.45"), true);
    }

   
    @Test
    public void testIsNumberWithNonNumericString() {
        assertEquals(Predicate.isNumber("abc123"), false);
    }

   
    @Test
    public void testIsNumberWithEmptyString() {
        assertEquals(Predicate.isNumber(""), false);
    }

    @Test
    public void testIsNumberWithNull() {
        assertEquals(Predicate.isNumber(null), false);
    }

    
    @Test
    public void testIsNumberWithLargeNumber() {
        assertEquals(Predicate.isNumber("12345678901234567890"), true);
    }

    
    @Test
    public void testIsNumberWithScientificNotation() {
        assertEquals(Predicate.isNumber("1e10"), true);
    }

    /**
     *  TESTS PARA LA FUNCION evaluate 
     */

    @Test
    public void testEvaluateWithEqualOperatorAndEqualValues() {
        Predicate predicate = new Predicate();
        assertEquals(predicate.evaluate("equal", Arrays.asList("3", "3", "3")), true);
    }

  
    @Test
    public void testEvaluateWithEqualOperatorAndDifferentValues() {
        Predicate predicate = new Predicate();
        assertEquals(predicate.evaluate("equal", Arrays.asList("3", "3", "4")), false);
    }

    
    @Test
    public void testEvaluateWithLessThanOperatorAndIncreasingOrder() {
        Predicate predicate = new Predicate();
        assertEquals(predicate.evaluate("<", Arrays.asList("1", "2", "3", "4")), true);
    }

   
    @Test
    public void testEvaluateWithLessThanOperatorAndNonIncreasingOrder() {
        Predicate predicate = new Predicate();
        assertEquals(predicate.evaluate("<", Arrays.asList("1", "3", "2", "4")), false);
    }

    
    @Test
    public void testEvaluateWithGreaterThanOperatorAndDecreasingOrder() {
        Predicate predicate = new Predicate();
        assertEquals(predicate.evaluate(">", Arrays.asList("4", "3", "2", "1")), true);
    }

  
    @Test
    public void testEvaluateWithGreaterThanOperatorAndNonDecreasingOrder() {
        Predicate predicate = new Predicate();
        assertEquals(predicate.evaluate(">", Arrays.asList("4", "3", "5", "2")), false);
    }

    
    @Test
    public void testEvaluateWithEqualOperatorAndDecimalValues() {
        Predicate predicate = new Predicate();
        assertEquals(predicate.evaluate("equal", Arrays.asList("3.14", "3.14", "3.14")), true);
    }

    
    @Test
    public void testEvaluateWithLessThanOperatorAndDecimalValuesInIncreasingOrder() {
        Predicate predicate = new Predicate();
        assertEquals(predicate.evaluate("<", Arrays.asList("1.1", "2.2", "3.3")), true);
    }

    
    @Test
    public void testEvaluateWithGreaterThanOperatorAndDecimalValuesInDecreasingOrder() {
        Predicate predicate = new Predicate();
        assertEquals(predicate.evaluate(">", Arrays.asList("3.3", "2.2", "1.1")), true);
    }

    
    @Test
    public void testEvaluateWithUnknownOperator() {
        Predicate predicate = new Predicate();
        assertEquals(predicate.evaluate("unknown", Arrays.asList("1", "2", "3")), false);
    }

    
    @Test
    public void testEvaluateWithEqualOperatorAndDifferentTypes() {
        Predicate predicate = new Predicate();
        assertEquals(predicate.evaluate("equal", Arrays.asList("3", "3", "three")), false);
    }
    
}
