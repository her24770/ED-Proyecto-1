import java.util.List;

public class Predicate {

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

    //funcion para verificar si es un numero
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