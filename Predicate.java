public class Predicate {

    public boolean evaluate(String operator,String value1, String value2) {
        switch (operator) {
            case "equal":
                return value1.equals(value2);
            case "<":
                return Double.parseDouble(value1) < Integer.parseInt(value2);
            case ">":
                return Integer.parseInt(value1) > Integer.parseInt(value2);
            default:
                return false;
        }
    }
    
}