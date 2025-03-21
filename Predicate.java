import java.util.List;

/**
 * Clase que define un predicado y evalua si se cumple
 */

public class Predicate {

    /**
     * Evalua si se cumple el predicado
     * @param operator operador del predicado
     * @param values valores a evaluar
     * @return true si se cumple, false si no
     */

    public boolean evaluate(String operator, List<String> values) {
        
        switch (operator) {
            case "equal":
                // Verifica si todos los valores son iguales
                for (int i = 1; i < values.size(); i++) {
                    if(isNumber(values.get(i))&&isNumber(values.get(0))){
                        if(Double.parseDouble(values.get(i))!=Double.parseDouble(values.get(0))){
                            return false;
                        }
                    }else{
                        if (!values.get(i).equals(values.get(0))) {
                            return false;
                        }
                    }
                    
                }
                return true;

            case "<":
                // Verifica si los valores están en orden estrictamente creciente
                for (int i = 1; i < values.size(); i++) {
                    if (Double.parseDouble(values.get(i - 1)) >= Double.parseDouble(values.get(i))) {
                        return false;
                    }
                }
                return true;

            case ">":
                // Verifica si los valores están en orden estrictamente decreciente
                for (int i = 1; i < values.size(); i++) {
                    if (Double.parseDouble(values.get(i - 1)) <= Double.parseDouble(values.get(i))) {
                        return false;
                    }
                }
                return true;

            default:
                return false;
        }
    }

    /**
    * Verifica si un string es un número
    * @param input string a verificar
    * @return true si es un número, false si no
    */
    public static boolean isNumber(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}