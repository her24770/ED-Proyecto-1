/**
 * Clase que representa una expresión QUOTE, y permite identificar si es un átomo o una lista
 */
public class Quote {

    private StringBuilder expresion; // Expresión a guardar

    // Constructor
    public Quote() {
        expresion = new StringBuilder();
    }
    // Getter para obtener la expresión sin evaluar
    public String getExpresion() {
        return expresion.toString();
        
    }

    public void setExpresion(StringBuilder expresion) {
        this.expresion = expresion;
    }

    /**
     * Método para identificar si la expresión es un átomo
     * @return true si es un átomo, false si no
     */

    public boolean isAtom() {
        String exp = expresion.toString().trim();
    
        // Remover TODOS los paréntesis externos 
        while (exp.startsWith("(") && exp.endsWith(")")) {
            exp = exp.substring(1, exp.length() - 1).trim();
        }
    
        // Verificar si luego de quitar paréntesis queda un solo elemento y no es una lista
        String[] elements = exp.split("\\s+"); // Divide por espacios
    
        return elements.length == 1 && !elements[0].startsWith("(");
    }

    /**
     * Método para identificar si la expresión es una lista
     * @return true si es una lista, false si no
     */

    public boolean isList(){
        String exp = expresion.toString().trim();
    
        
        while (exp.startsWith("(") && exp.endsWith(")")) {
            exp = exp.substring(1, exp.length() - 1).trim();
        }
    
        // Verificar si luego de quitar paréntesis queda un solo elemento y no es una lista
        String[] elements = exp.split("\\s+"); // Divide por espacios
    
        return elements.length != 1 && !elements[0].startsWith("(");
        
    }


    /** 
     * Método para obtener la expresión QUOTE en forma de lista
     * @return expresión QUOTE en forma de lista
     */
    @Override
    public String toString() {
        return "'" + expresion;  // El prefijo ' indica que es una expresión QUOTE
    }

    /**
     * Método para obtener la expresión QUOTE en forma de átomo
     * @return expresión QUOTE en forma de átomo
     */
    public String toAtom(){
        return "" + expresion;
    }


}