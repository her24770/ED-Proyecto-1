public class Quote {
    private String expresion;

    // Constructor
    public Quote(String expresion) {
        this.expresion = expresion;
    }

    // Getter para obtener la expresión sin evaluar
    public String getQuote() {
        return expresion;
    }

    // Setter para asignar una nueva expresión
    public void setQuote(String expresion) {
        this.expresion = expresion;
    }

    // Método toString para representar la expresión como una cadena
    @Override
    public String toString() {
        return "'" + expresion;  // El prefijo ' indica que es una expresión QUOTE
    }
}