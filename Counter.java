public class Counter{
    private int count = 0;
    private String value;
    private String arithmeticValue;

    

    public void setArithmeticValue(double arithmeticValue) {
        this.arithmeticValue = String.valueOf(arithmeticValue);
    }

    public String getArithmeticValue() {
        return arithmeticValue;
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
