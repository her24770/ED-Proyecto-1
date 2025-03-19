import java.util.List;

public class Calculator {

    
    /**
     * Realiza la operacion matematica dentro de la lista de valores
     * @param operator operador matematico
     * @param values lista de valores
     * @return resultado de la operacion
     */
    public double operation(String operator, List<String> values) {
        if (values.isEmpty()) {
            Parser.exitForErrorSintax(5);
        }
        else if(values.size() < 2){
            Parser.exitForErrorSintax(5);
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
                        Parser.exitForErrorSintax(6);;
                    }
                    value /= current;
                    break;
                default:
                    Parser.exitForErrorSintax(3);
            }
        }
    
        return value;
    }
}