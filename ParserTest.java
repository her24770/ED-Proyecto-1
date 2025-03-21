import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;


public class ParserTest {

    /**
     * Test para recorrimiento, algunos tienen verificacion de valores pero otros se validan con otras funciones
     */
    @Test
    public void testExecuteKeyWordsWithSetq() {
        ArrayList<String> tokens = new ArrayList<>();
        tokens.add("(");
        tokens.add("setq");
        tokens.add("x");
        tokens.add("10");
        tokens.add(")");

        Counter logic = new Counter();
        Parser instance = new Parser();

        logic = instance.executeKeyWords(tokens, logic);

        // Verificar que el contador de tokens se haya incrementado correctamente
        assertEquals(4, logic.getCount()); 
    }


    @Test
    public void testExecuteKeyWordsWithQuote() {
        ArrayList<String> tokens = new ArrayList<>();
        tokens.add("(");
        tokens.add("quote");
        tokens.add("x");
        tokens.add(")");

        
        Counter logic = new Counter();
        Parser instance = new Parser();

        logic = instance.executeKeyWords(tokens, logic);

        // Verificar que el contador de tokens se haya incrementado correctamente
        assertEquals(2, logic.getCount()); 
    }

    
    @Test
    public void testExecuteKeyWordsWithAtom() {
        ArrayList<String> tokens = new ArrayList<>();
        tokens.add("(");
        tokens.add("atom");
        tokens.add("x");
        tokens.add(")");

        Counter logic = new Counter();
        Parser instance = new Parser();

        logic = instance.executeKeyWords(tokens, logic);

        // Verificar que el contador de tokens se haya incrementado correctamente
        assertEquals(2, logic.getCount()); 

        // Verificar que la operación "atom" sea correcta
        assertEquals(true, logic.isValueBool()); 
    }

    @Test
    public void testExecuteKeyWordsWithArithmeticOperations() {
        ArrayList<String> tokens = new ArrayList<>();
        tokens.add("(");
        tokens.add("+");
        tokens.add("2");
        tokens.add("3");
        tokens.add(")");

        Counter logic = new Counter();
        Parser instance = new Parser();

        logic = instance.executeKeyWords(tokens, logic);

        // Verificar que el contador de tokens se haya incrementado correctamente
        assertEquals(4, logic.getCount()); 

       //verificador de suma
        assertEquals(Double.valueOf(5), Double.valueOf(logic.getValue())); 
    }

    @Test
    public void testExecuteKeyWordsWithEqual() {
        ArrayList<String> tokens = new ArrayList<>();
        tokens.add("(");
        tokens.add("equal");
        tokens.add("10");
        tokens.add("10");
        tokens.add(")");

        Counter logic = new Counter();
        Parser instance = new Parser();

       
        logic = instance.executeKeyWords(tokens, logic);

        // Verificar que el contador de tokens se haya incrementado correctamente
        assertEquals(4, logic.getCount()); 

        // Verificar que la operación "equal" sea verdadero
        assertEquals(logic.isValueBool(), true); 
    }

    @Test
    public void testListWithExpression() {
        ArrayList<String> tokens = new ArrayList<>();
        tokens.add("(");
        tokens.add("list");
        tokens.add("("); 
        tokens.add("setq");
        tokens.add("x");
        tokens.add("10");
        tokens.add(")"); 
        tokens.add(")");

        
        Counter logic = new Counter();
        Parser instance = new Parser();

        logic = instance.list(tokens, logic);

        // Verificar que el contador de tokens se haya incrementado correctamente
        assertEquals(7, logic.getCount());
    }
}
    
