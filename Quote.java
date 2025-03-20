public class Quote {

    private StringBuilder expresion;

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


    public boolean isList(){
        String exp = expresion.toString().trim();
    
        
        while (exp.startsWith("(") && exp.endsWith(")")) {
            exp = exp.substring(1, exp.length() - 1).trim();
        }
    
        // Verificar si luego de quitar paréntesis queda un solo elemento y no es una lista
        String[] elements = exp.split("\\s+"); // Divide por espacios
    
        return elements.length != 1 && !elements[0].startsWith("(");
        
    }


    // Setter para asignar una nueva expresión

    // Método toString para representar la expresión como una cadena
    @Override
    public String toString() {
        return "'" + expresion;  // El prefijo ' indica que es una expresión QUOTE
    }

    public String toAtom(){
        return "" + expresion;
    }


}