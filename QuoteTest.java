import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class QuoteTest {

    // Caso 1: Verificar si la expresión es un átomo (solo un elemento sin paréntesis)
    @Test
    public void testIsAtomWithSingleElement() {
        Quote quote = new Quote();
        quote.setExpresion(new StringBuilder("hello"));
        assertEquals(quote.isAtom(), true);
    }

    // Caso 2: Verificar si la expresión no es un átomo (contiene más de un elemento o paréntesis)
    @Test
    public void testIsAtomWithMultipleElements() {
        Quote quote = new Quote();
        quote.setExpresion(new StringBuilder("(hello world)"));
        assertEquals(quote.isAtom(), false);
    }

    // Caso 3: Verificar si la expresión es una lista (tiene más de un elemento sin paréntesis)
    @Test
    public void testIsListWithMultipleElements() {
        Quote quote = new Quote();
        quote.setExpresion(new StringBuilder("hello world"));
        assertEquals(quote.isList(), true);
    }

    // Caso 4: Verificar si la expresión no es una lista (solo un elemento)
    @Test
    public void testIsListWithSingleElement() {
        Quote quote = new Quote();
        quote.setExpresion(new StringBuilder("hello"));
        assertEquals(quote.isList(), false);
    }

    // Caso 5: Verificar el comportamiento de la expresión con paréntesis externos (con un solo elemento)
    @Test
    public void testIsAtomWithParentheses() {
        Quote quote = new Quote();
        quote.setExpresion(new StringBuilder("(hello)"));
        assertEquals(quote.isAtom(), true);
    }

    // Caso 6: Verificar el comportamiento de la expresión con paréntesis externos (con múltiples elementos)
    @Test
    public void testIsListWithParentheses() {
        Quote quote = new Quote();
        quote.setExpresion(new StringBuilder("(hello world)"));
        assertEquals(quote.isList(), true);
    }

    // Caso 7: Verificar el método toString
    @Test
    public void testToString() {
        Quote quote = new Quote();
        quote.setExpresion(new StringBuilder("hello world"));
        assertEquals("'hello world", quote.toString(), "'hello world");
    }

    // Caso 8: Verificar el método toAtom
    @Test
    public void testToAtom() {
        Quote quote = new Quote();
        quote.setExpresion(new StringBuilder("hello world"));
        assertEquals("hello world", quote.toAtom(), "hello world");
    }

    // Caso 9: Verificar la expresión con espacio vacío
    @Test
    public void testIsAtomWithEmptyExpression() {
        Quote quote = new Quote();
        quote.setExpresion(new StringBuilder("   "));
        assertEquals(quote.isAtom(), true);
    }
}

