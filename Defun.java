import java.util.ArrayList;
import java.util.HashMap;

/**
 * Clase que define una funcion
 */ 

public class Defun{

    // Atributos
    private String nombre; // Nombre de la funcion
    private ArrayList<String> parametros; // Parametros de la funcion
    private HashMap<String,String> variables= new HashMap<String,String>(); // Variables de la funcion
    private ArrayList<String> cuerpo; // Cuerpo de la funcion
    /**
     * Constructor de la clase
     */
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

    /**
     * Metodo que agrega una variable a la funcion
     * @param key
     * @param value
     */ 
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