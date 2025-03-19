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
    
        // Verificar si es una lista válida con paréntesis
        if (exp.startsWith("(") && exp.endsWith(")")) {
            // Eliminar los paréntesis
            exp = exp.substring(1, exp.length() - 1).trim();
    
            // Verificar si contiene un solo elemento
            String[] elements = exp.split("\\s+"); // Divide por espacios
    
            return elements.length == 1 && !elements[0].startsWith("(");
        }
        
        // Si no es una lista, ya es un átomo
        return true;
    }
    

    // Setter para asignar una nueva expresión

    // Método toString para representar la expresión como una cadena
    @Override
    public String toString() {
        return "'" + expresion;  // El prefijo ' indica que es una expresión QUOTE
    }
}