public class Calculator<T> {

    // Constructor privado para evitar instanciación externa
    private Calculator() {}

    // Método de operación genérica
    public T operation(char operator, T value1, T value2) {
        switch (operator) {
            case '+':
                return (T) (Double) ((Double) value1 + (Double) value2);
            case '-':
                return (T) (Double) ((Double) value1 - (Double) value2);
            case '*':
                return (T) (Double) ((Double) value1 * (Double) value2);
            case '/':
                return (T) (Double) ((Double) value1 / (Double) value2);
            case '%':
                return (T) (Double) ((Double) value1 % (Double) value2);
            case '^':
                return (T) (Double) Math.pow((Double) value1, (Double) value2);
            default:
                return null;
        }
    }
}