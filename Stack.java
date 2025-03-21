import java.util.ArrayList;

/**
 * Clase que define una pila
 * @param <T>
 */

public class Stack<T>{

    protected ArrayList<T> data; // Datos de la pila

    public Stack(){
        data = new ArrayList<T>();
    }

    /**
     * Metodo que agrega un valor a la pila
     * @param value
     */

    public void push(T value){
        data.add(value);
    }

    /**
     * Metodo que obtiene el ultimo valor de la pila y lo elimina
     * @return ultimo valor de la pila
     */

    public T pop(){
        if(data.size() != 0){
            return data.removeLast();
        }
        return null;
    }

    /**
     * Metodo retorna el tamaño de la pila
     * @return tamaño de la pila
     */
    public int size(){
        return data.size();
    }

    /**
     * Metodo que verifica si la pila esta vacia
     * @return true si esta vacia, false si no
     */

    public boolean isEmpty() {
        return data.isEmpty();
    }
}