public class Counter{
    private int count = 0;
    private String value;
    private boolean valueBool;
    private SetQ vairablesLocales= new SetQ();
    

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

    public Counter() {
        count = 0;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

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
