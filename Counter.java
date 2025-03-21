/**
 * Clase que se encarga de llevar el conteo de las instrucciones que se van ejecutando
 * y de llevar el control de las variables locales
 * 
 */
public class Counter{


    private int count = 0; //Contador de instrucciones
    private String value; // Valor de la instruccion
    private boolean valueBool; // Valor booleano de la instruccion
    private SetQ vairablesLocales= new SetQ(); // Variables locales de la instruccion
    

    public SetQ getVairablesLocales() {
        return vairablesLocales;
    }

    public void setVairablesLocales(SetQ vairablesLocales) {
        this.vairablesLocales = vairablesLocales;
    }

    public void addVariableLocal(String variable,String value){
        vairablesLocales.assign(variable, value);
    }

    public boolean isValueBool() {
        return valueBool;
    }

    public void setValueBool(boolean valueBool) {
        this.valueBool = valueBool;
    }

    /**
     * Constructor de la clase
     */

    public Counter() {
        count = 0;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Incrementa el contador de instrucciones
     * @param increase
     */

    public void increment(int increase) {
        count = count+ increase;
    }

    

    public int getCount() {
        return count;
    }
    public int setCount(int count) {
        return this.count = count;
    }
    
}
