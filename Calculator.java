import java.util.List;

public class Calculator {

    // Constructor privado para evitar instanciación externa
    public Calculator() {
        
    }

    // Método de operación genérica
    public double operation(String operator, List<String> values) {
        if (values.isEmpty()) {
            throw new IllegalArgumentException("No se encontraron valores para operar.");
        }
        
        //primer valor de la operacion
        double value = Double.parseDouble(values.get(0));
    
        for (int i = 1; i < values.size(); i++) {
            double current = Double.parseDouble(values.get(i)); //valor actual
    
            switch (operator) {
                case "+":
                    value += current;
                    break;
                case "-":
                    value -= current;
                    break;
                case "*":
                    value *= current;
                    break;
                case "/":
                    if (current == 0) {
                        throw new ArithmeticException("Division sobre cero.");
                    }
                    value /= current;
                    break;
                default:
                    throw new IllegalArgumentException("No se reconoce: " + operator);
            }
        }
    
        return value;
    }
}