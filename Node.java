 public class Node<T> {

    protected T value; 
    protected Node<T> nodeNext; 
 
    //constructor para LinkedList
    public Node(T v, Node<T> next) {
        value = v;
        nodeNext = next;
    }
  
 
    public Node<T> getNext(){
       return nodeNext;
    }
  
     public void setNext(Node<T> next){
         nodeNext = next;
     }
 
    public T getValue(){
       return value;
    }
 
    public void setValue(T v){
       value = v;
    }
}