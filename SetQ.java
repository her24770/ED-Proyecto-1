import java.util.HashMap;

/**
 * Clase que define un conjunto de variables
 */

public class SetQ {
    private HashMap<String, String> variables; // Variables del conjunto

    public SetQ() {
        this.variables = new HashMap<>();
    }

    /**
     * Metodo que asigna un valor a una variable
     * @param variable
     * @param value
     */
    public void assign(String variable, String value) {
        variables.put(variable, value);
    }

    // Método para obtener el valor de una variable
    public String getValue(String variable) {
        return variables.get(variable);
    }

    // Método para imprimir todas las variables (opcional, para debugging)
    public void printVariables() {
        for (String key : variables.keySet()) {
            System.out.println(key + " = " + variables.get(key));
        }
    }
}