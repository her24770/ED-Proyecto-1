import java.util.HashMap;

public class SetQ {
    private HashMap<String, String> variables;

    public SetQ() {
        this.variables = new HashMap<>();
    }

    // Método para asignar valores a las variables
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