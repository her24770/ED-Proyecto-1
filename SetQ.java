import java.util.HashMap;

public class SetQ<T> {
    private HashMap<String, T> variables;

    // Constructor
    public SetQ() {
        this.variables = new HashMap<>();
    }

    // Método para asignar un valor a una variable
    public void setNombre(String nombre, T valor) {
        variables.put(nombre, valor);
    }

    // Método para obtener el valor de una variable
    public T getNombre(String nombre) {
        return variables.get(nombre);
    }

    // Método para obtener el mapa completo de variables
    public HashMap<String, T> getVariables() {
        return variables;
    }

    // Método para establecer el mapa completo de variables
    public void setVariables(HashMap<String, T> variables) {
        this.variables = variables;
    }

}