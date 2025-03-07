import java.util.ArrayList;

public class Stack<T>{

    protected ArrayList<T> data;

    public Stack(){
        data = new ArrayList<T>();
    }

    public void push(T value){
        data.add(value);
    }

    public T pop(){
        if(data.size() != 0){
            return data.removeLast();
        }
        return null;
    }

    public int size(){
        return data.size();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }
}