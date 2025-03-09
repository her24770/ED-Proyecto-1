import java.util.ArrayList;
import java.util.HashMap;

public class Defun<T> {

    // Atributos
    private String nombre;
    private ArrayList<HashMap<String, String>> parametros;
    private ArrayList<String> variables;
    private ArrayList<String> cuerpo;
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public ArrayList<HashMap<String, String>> getParametros() {
        return parametros;
    }
    public void setParametros(ArrayList<HashMap<String, String>> parametros) {
        this.parametros = parametros;
    }
    public ArrayList<String> getVariables() {
        return variables;
    }
    public void setVariables(ArrayList<String> variables) {
        this.variables = variables;
    }
    public ArrayList<String> getCuerpo() {
        return cuerpo;
    }
    public void setCuerpo(ArrayList<String> cuerpo) {
        this.cuerpo = cuerpo;
    }

    
}