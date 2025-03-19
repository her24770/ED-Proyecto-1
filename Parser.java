
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    List<String> KEYWORDS = new ArrayList<>(Arrays.asList(
        "<", ">", "+", "-", "*", "/", "^", "(", ")", "'","=", 
        "defun", "quote", "setq", "cond", "atom", "list", "equal"
    ));

    ArrayList<String> tokens = new ArrayList<>();
    ArrayList<Defun> functions = new ArrayList<>();
    SetQ variables = new SetQ();


    
    
    //funcion para verificar si los parentesis estan cerrados
    public boolean cierreParentesis(String code) {
        Stack<Character> pila = new Stack<>();
    
        for (char c : code.toCharArray()) {
            if (c == '(') {
                pila.push(c); 
            } else if (c == ')') {
                if (pila.isEmpty() || pila.pop() != '(') {
                    return false; 
                }
            }
        }
        return pila.isEmpty(); 
    }

    //funcion para tokenizar
    public ArrayList<String> tokenize(String code) {

        ArrayList<String> tokens = new ArrayList<>();

        Pattern pattern = Pattern.compile("\\(|\\)|'|\\w+|[-+*/^<>=]");
        Matcher matcher = pattern.matcher(code);

        while (matcher.find()) {
            tokens.add(matcher.group());
        }

        return tokens;
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
    
    
    public static void exitForErrorSintax(int typeError){
        switch (typeError) {
            case 1:
                System.out.println("Error: Al abrir parentesis");
                break;
            case 2:
                System.out.println("Error: Al cerrar parentesis");
                break;
            case 3:
                System.out.println("Error: Al no encontrar KEYWORDS");
                break;
            case 4:
                System.out.println("Error: Al declarar variables");
                break;
        
            default:
                System.out.println("Error: Sintaxis de Lisp incorrecta");
                break;
        }
        System.exit(0);
    }

    boolean globalEnviroment = true;

    public void execute(ArrayList<String> tokens) {
        for(int i=0; i<tokens.size(); i++){
            
            if(tokens.get(i).equals("(")&& globalEnviroment==true){
                globalEnviroment=false;
                if(tokens.get(i+1).equals("defun")){
                    Defun defunNew = new Defun();
                    defunNew.setNombre(tokens.get(i+2));
                    if(tokens.get(i+3).equals("(")){
                        i=i+4;
                        ArrayList<String> parametros = new ArrayList<>();
                        while(!tokens.get(i).equals(")")){
                            parametros.add(tokens.get(i));
                            i++;
                        }
                        ArrayList<String> cuerpo = new ArrayList<>();
                        int contadorBrackets = 0;
                        i++;
                        while(contadorBrackets>=0){
                            if(tokens.get(i).equals("(")){
                                contadorBrackets++;
                            }else if(tokens.get(i).equals(")")){
                                contadorBrackets--;
                            }
                            if (contadorBrackets>=0){
                                cuerpo.add(tokens.get(i));
                                i++;
                            }
                        
                            
                        }
                        defunNew.setParametros(parametros);
                        defunNew.setCuerpo(cuerpo);
                        functions.add(defunNew);
                        globalEnviroment=true;
                    }
                }else if(KEYWORDS.contains(tokens.get(i+1))){
                    globalEnviroment=true;
                    Counter counterGlobal = new Counter();
                    counterGlobal.setCount(i);
                    counterGlobal=executeKeyWords(tokens, counterGlobal);                
                    i=counterGlobal.getCount();
                    
                }else{
                    exitForErrorSintax(3);
                }
            }else if(i>=tokens.size()){
                System.out.println("Fin del programa");
                System.exit(0);

            }else{
                exitForErrorSintax(1);
            }
        }
    }

    public Counter executeKeyWords(ArrayList<String> tokens, Counter logic){
        
        if(logic.getCount()>=tokens.size()){
            System.out.println("Error: Fin del programa");
            System.exit(0);
        }
        switch (tokens.get(logic.getCount() + 1)) {
            case "setq":
                logic =setq(tokens, logic);
                break;
                
            case "quote":

                logic = quote(tokens, logic);
                break;
                
            case "cond":
                
            case "atom":
            case "list":

            case "equal":
            case "<":
            case ">":
                logic=predicates(tokens,logic);
                break;
                
                
            case "+":
            case "-":
            case "*":
            case "/":
                logic=arithmeticOperation(tokens, logic);
                System.out.println(logic.getArithmeticValue());
                break;
        
            default:
                break;
        }
        return logic;
    }

    public Counter quote(ArrayList<String> tokens, Counter logic) {
        StringBuilder quoteBodyBuilder = new StringBuilder();
        int i = logic.getCount() + 2; 
    
        if (i < tokens.size() && tokens.get(i).equals("(")) {
            int parentesisHandler = 1;

            quoteBodyBuilder.append(tokens.get(i)).append(" ");
            i++;
            
            while (i < tokens.size() && parentesisHandler > 0) {
                String token = tokens.get(i);
                if (token.equals("(")){
                    parentesisHandler++;
                }
                
                    
                else if (token.equals(")")){
                
                    parentesisHandler--;
                }
                
                quoteBodyBuilder.append(token).append(" ");
                i++;

            }
            
        } else {
            System.out.println("Error: quote sin argumento");
            
        }

        System.out.println("\nQUOTE: " + quoteBodyBuilder.toString() + "");
        logic.increment(i - logic.getCount());

        return logic;
    }

    public int aritmetic(ArrayList<String> tokens, int i){
        i=i+2;
        while(true){
            if (tokens.get(i)==")"){
                i++;
                return i;
            }else if(KEYWORDS.contains(tokens.get(i))){
                
            }
        }
    }

    public Counter arithmeticOperation(ArrayList<String> tokens, Counter logic) {
        String operator = tokens.get(logic.getCount() + 1);
        List<String> values = new ArrayList<>();

        // Recopilar todos los valores hasta encontrar ")"
        int i = logic.getCount() + 2;
        while (i < tokens.size() && !tokens.get(i).equals(")")) {
            if (isNumber(tokens.get(i))) {
                values.add(tokens.get(i));
                i++;
            }
            else if(tokens.get(i).equals("(")){
                Counter counter = new Counter();
                counter.setCount(i);
                values.add(String.valueOf(arithmeticOperation(tokens, counter).getArithmeticValue()));
                i=counter.getCount()+1;
            }
            else if (variables.getValue(tokens.get(i))!=null){
                values.add(variables.getValue(tokens.get(i)));
                i++;
            }
            else{
                exitForErrorSintax(3); // Manejo de error sintáctico
            }
            
        }

        // Verificar si se encontró el cierre ")"
        if (i >= tokens.size() || !tokens.get(i).equals(")")) {
            exitForErrorSintax(2); // Manejo de error sintáctico
        }

        // Evaluar los valores con el operador
        Calculator calculator = new Calculator();
        Double result = calculator.operation(operator, values);
        // System.out.println(result);

        // Actualizar el contador
        logic.increment(i - logic.getCount());
        logic.setArithmeticValue(result);
        return logic;
    }


    public Counter predicates(ArrayList<String> tokens, Counter logic) {
        String operator = tokens.get(logic.getCount() + 1);
        List<String> values = new ArrayList<>();

        // Recopilar todos los valores hasta encontrar ")"
        int i = logic.getCount() + 2;
        while (i < tokens.size() && !tokens.get(i).equals(")")) {
            if (isNumber(tokens.get(i))) {
                values.add(tokens.get(i));
            }else if(variables.getValue(tokens.get(i))!=null){
                values.add(variables.getValue(tokens.get(i)));
            }
            i++;
        }

        // Verificar si se encontró el cierre ")"
        if (i >= tokens.size() || !tokens.get(i).equals(")")) {
            exitForErrorSintax(2); // Manejo de error sintáctico
        }

        // Evaluar los valores con el operador
        Predicate predicate = new Predicate();
        boolean result = predicate.evaluate(operator, values);
        System.out.println(result);
        // Actualizar el contador
        logic.increment(i - logic.getCount());
        return logic;
    }

    public Counter setq(ArrayList<String> tokens, Counter logic) {
        // Verificar que el primer token después de "setq" sea una variable
        int i = logic.getCount() + 2;
    
        // Procesar pares de variables y valores
        while (i < tokens.size() && !tokens.get(i).equals(")")) {
            String variable = tokens.get(i); // Nombre de la variable
            i++;
    
            if (i >= tokens.size()) {
                exitForErrorSintax(4); // Error si no hay valor para la variable
            }
            
            String value = tokens.get(i); // Valor de la variable
            i++;
            
            //validar que no sea variables o funcion
            if(variables.getValue(value)!=null){
                value=variables.getValue(value);
            }

    
            // Asignar la variable y su valor
            if(globalEnviroment){
                variables.assign(variable, value);
            }
        }
    
        // Verificar el cierre ")"
        if (i >= tokens.size() || !tokens.get(i).equals(")")) {
            exitForErrorSintax(4); // Manejo de error sintáctico
        }
    
        // Actualizar el contador
        logic.increment(i - logic.getCount());
    
        // Opcional: Imprimir las variables para debugging
        variables.printVariables();
    
        return logic;
    }
}