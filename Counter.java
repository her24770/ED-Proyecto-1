public class Counter{
    private int count = 0;
    private String value;
    

    

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
