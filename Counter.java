public class Counter{
    private int count = 0;
    private String value;
    private boolean valueBool;
    

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
