import java.util.ArrayList;
import java.util.HashMap;

public class Defun{

    // Atributos
    private String nombre;
    private ArrayList<String> parametros;
    private HashMap<String,String> variables= new HashMap<String,String>();
    private ArrayList<String> cuerpo;
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public ArrayList<String> getParametros() {
        return parametros;
    }
    public void setParametros(ArrayList<String> parametros) {
        this.parametros = parametros;
    }
    public HashMap<String,String> getVariables() {
        return variables;
    }
    public void setVariables(HashMap<String,String> variables) {
        this.variables = variables;
    }
    public void addVariable(String key, String value){
        this.variables.put(key, value);
    }

    public ArrayList<String> getCuerpo() {
        return cuerpo;
    }
    public void setCuerpo(ArrayList<String> cuerpo) {
        this.cuerpo = cuerpo;
    }

    
}