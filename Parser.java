
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Condition;
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
            case 5:
                System.out.println("Error: Al no encontrar valores para operar");
                break;
            case 6: 
                System.out.println("Error: Numero de parametros incorrecto");
                break;
            
            case 7:
                System.out.println("Error: Al redactar condicion");
        
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
        if (searchDefun(functions, tokens.get(logic.getCount()))!=null){
            
            
            // ArrayList para guardar los parámetros
            ArrayList<String> parametrosSend = new ArrayList<>();
            Defun defunRun =searchDefun(functions, tokens.get(logic.getCount()));
            logic.increment(1);
            // Leemos los tokens hasta encontrar un paréntesis de cierre
            while (!tokens.get(logic.getCount()).equals(")")) {
                // Validar los parametros y setearlos como variables
                if(variables.getValue(tokens.get(logic.getCount()))!=null){
                    parametrosSend.add(variables.getValue(tokens.get(logic.getCount())));
                }else{
                    parametrosSend.add(tokens.get(logic.getCount()));
                }
                logic.increment(1);
            }

            System.out.println("Lista de parametros");
            for (int i = 0; i < parametrosSend.size(); i++) {
                System.out.println(parametrosSend.get(i));
            }
            //error en caso de numero invalido de parametros
            if(parametrosSend.size()!=defunRun.getParametros().size()){
                exitForErrorSintax(6);
            }            
            // Avanzamos el contador para saltar el paréntesis de cierre
            //Counter counterDefun = new Counter();
            //logic=defun(defunRun, counterDefun,parametrosSend);
            
        }else{
            switch (tokens.get(logic.getCount() + 1)) {
                case "setq":
                    logic =setq(tokens, logic);
                    break;
                    
                case "quote":

                    logic = quote(tokens, logic);
                    break;
                    
                case "cond":
                    logic = cond(tokens, logic);
                    logic.increment(1);
                    break;
                    
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
                    System.out.println(logic.getValue());
                    break;
            
                default:
                    break;
            }
        }
        return logic;
    }

    public Counter quote(ArrayList<String> tokens, Counter logic) {
        StringBuilder quoteBodyBuilder = new StringBuilder();
        Quote quote = new Quote();
        int i = logic.getCount() + 2; 
        if (tokens.get(i).equals("(")) {
            int parentesisHandler = 1;
            quoteBodyBuilder.append(tokens.get(i)).append(" ");
            i++;
            while (parentesisHandler > 0) {
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
        quote.setExpresion(quoteBodyBuilder);
        System.out.print(quote.toString());
        logic.increment(i - logic.getCount());
        return logic;
    }

    public Counter atom(ArrayList<String> tokens, Counter logic, int opcion) {
        int i = logic.getCount() + 2; 
        Quote quote = new Quote();
        
        if (i < tokens.size() && (tokens.get(i).equals("quote") || tokens.get(i).equals("'"))) {
            i++; 
        }
        
        StringBuilder quoteBody = new StringBuilder();
        int parentesisHandler = 0;
    
        if (tokens.get(i).equals("(")) {
            parentesisHandler = 1;
            quoteBody.append(tokens.get(i)).append(" ");
            i++;
    
            if (KEYWORDS.contains(tokens.get(i))) {
                logic.setCount(i);
                logic = executeKeyWords(tokens, logic);
                String value = logic.getValue();
                quoteBody.append(value).append(" ");
                System.out.println("Valor: "+value);
            }
    
            while (parentesisHandler > 0 && i < tokens.size()) {
                String token = tokens.get(i);
    
                if (token.equals("(")) {
                    parentesisHandler++;
                } else if (token.equals(")")) {
                    parentesisHandler--;
                }
                quoteBody.append(token).append(" ");
                i++;
            }
        }  
        quote.setExpresion(quoteBody);
        System.out.print(quote.isAtom());
        logic.increment(i - logic.getCount());
        return logic;
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
                values.add(String.valueOf(arithmeticOperation(tokens, counter).getValue()));
                i=counter.getCount()+1;
            }
            else if (variables.getValue(tokens.get(i))!=null){
                values.add(variables.getValue(tokens.get(i)));
                i++;
            }
            else if(!tokens.get(i).equals(")")){
                exitForErrorSintax(5); // Manejo de error sintáctico
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
        logic.setValue(result.toString());
        return logic;
    }


    public Counter predicates(ArrayList<String> tokens, Counter logic) {
        String operator = tokens.get(logic.getCount() + 1);
        List<String> values = new ArrayList<>();

        // Recopilar todos los valores hasta encontrar ")"
        logic.increment(2);
        while (logic.getCount() < tokens.size() && !tokens.get(logic.getCount() ).equals(")")) {
            if (isNumber(tokens.get(logic.getCount() ))) {
                values.add(tokens.get(logic.getCount() ));
            }else if(variables.getValue(tokens.get(logic.getCount() ))!=null){
                values.add(variables.getValue(tokens.get(logic.getCount() )));
            }else if(tokens.get(logic.getCount() ).equals("(")){
                logic = executeKeyWords(tokens, logic);
                values.add(logic.getValue());
            }
            logic.increment(1);;
        }

        // Verificar si se encontró el cierre ")"
        if (logic.getCount()  >= tokens.size() || !tokens.get(logic.getCount() ).equals(")")) {
            exitForErrorSintax(2); // Manejo de error sintáctico
        }

        // Evaluar los valores con el operador
        Predicate predicate = new Predicate();
        boolean result = predicate.evaluate(operator, values);
        logic.setValueBool(result);
        System.out.println(result);
        return logic;
    }

    public Counter setq(ArrayList<String> tokens, Counter logic) {
                // String value = "";
        // Verificar que el primer token después de "setq" sea una variable
        logic.increment(2);
    
        // Procesar pares de variables y valores
        while (logic.getCount() < tokens.size() && !tokens.get(logic.getCount()).equals(")")) {
            String variable = tokens.get(logic.getCount()); // Nombre de la variable
            logic.increment(1);;
    
            if (logic.getCount() >= tokens.size()) {
                exitForErrorSintax(4); // Error si no hay valor para la variable
            }
            
            String value = tokens.get(logic.getCount()); // Valor de la variable
            
             //validar que no sea variables o funcion
             
             if (tokens.get(logic.getCount()).equals("(")) {
                 Defun defunsearch = searchDefun(functions, tokens.get(logic.getCount()+1));
                
                 if (defunsearch!=null){
                     logic.increment(1);
                     logic = executeKeyWords(tokens, logic);
                     value=logic.getValue();
                     System.out.println(logic.getCount());
                 }
                
             }

            //validar variables
            if(variables.getValue(value)!=null){
                value=variables.getValue(value);
            }

            
            logic.increment(1);;

    
            // Asignar la variable y su valor
            if(globalEnviroment){
                variables.assign(variable, value);
            }
        }
        
    
        // Verificar el cierre ")"
        if (logic.getCount() >= tokens.size() || !tokens.get(logic.getCount()).equals(")")) {
            exitForErrorSintax(4); // Manejo de error sintáctico
        }
    
        // Opcional: Imprimir las variables para debugging
        variables.printVariables();
    
        return logic;
    }

    public Defun searchDefun(ArrayList<Defun> functions, String nombre) {
        for (Defun defun : functions) {
            if(defun.getNombre().equals(nombre)) {
                return defun;
            }
        }
        return null;
    }

    public Counter defun(Defun defunRun, Counter logic, ArrayList<String> parametros){ 
        //manejar funcion en el global
        /*for(int i=0; i<defunRun.getParametros().size(); i++){
            if(!isNumber(defunRun.getParametros().get(i))){
                if(variables.getValue(defunRun.getParametros().get(i))!=null){
                    variables.assign(defunRun.getParametros().get(i), variables.getValue(defunRun.getParametros().get(i)));       
                }
            }
        }*/
         //System.out.println("Lista de parametros");
        // for (int i = 0; i < parametros.size(); i++) {
        //     System.out.println(parametros.get(i));
        // }
         System.out.println("Defun");
        //manejar funcion interna
        return logic;

    }
    public Counter cond(ArrayList<String> tokens, Counter logic){
        logic.increment(2); //Saltar lo verificado en executeKeyWords

        //Verificar estrucutura de COND (doble parentesis)
        if(tokens.get(logic.getCount()).equals("(") && tokens.get(logic.getCount()+1).equals("(")){
            logic.increment(1);
    
            // Verificar si es una condición válida
            if(!tokens.get(logic.getCount()+1).equals(")") && !tokens.get(logic.getCount()+1).equals("(") &&
               !tokens.get(logic.getCount()+1).equals("setq") && !tokens.get(logic.getCount()+1).equals("defun") &&
               !tokens.get(logic.getCount()+1).equals("cond") && !tokens.get(logic.getCount()+1).equals("quote") && !tokens.get(logic.getCount()+1).equals("t")){
                
                //verifica si el comparador es valido
                if(!KEYWORDS.contains(tokens.get(logic.getCount()+1))){
                    exitForErrorSintax(7);
                }

                int parenthesisCount = 1;
                ArrayList<String> condition = new ArrayList<>();
    
                // Separar la condición
                while(parenthesisCount != 0){
                    if(tokens.get(logic.getCount()+1).equals("(")){
                        parenthesisCount++;
                    }else if(tokens.get(logic.getCount()+1).equals(")")){
                        parenthesisCount--;
                    }
                    condition.add(tokens.get(logic.getCount()));
                    logic.increment(1);
                }
                condition.add(")"); // Agrega el paréntesis de cierre para usar executeKeyWords
                logic.increment(1);  
    
                //Separar cada instruccion y meterlas en una
                ArrayList<ArrayList<String>> instructionList = new ArrayList<>();
                while(!tokens.get(logic.getCount()).equals(")")){
                    if(tokens.get(logic.getCount()).equals("(")){
                        ArrayList<String> instruction = new ArrayList<>();
                        int parenthesisCounter = 1;
                        instruction.add(tokens.get(logic.getCount()));
                        logic.increment(1);
                        while(parenthesisCounter != 0){
                            String token = tokens.get(logic.getCount());
                            if(token.equals("(")){
                                parenthesisCounter++;
                            } else if(token.equals(")")){
                                parenthesisCounter--;
                            }
                            instruction.add(token);
                            logic.increment(1);
                        }
                        instructionList.add(instruction);
                    } 
                    //para agregar los tokens fuera de parentesis
                    else {
                        ArrayList<String> instruccion = new ArrayList<>();
                        instruccion.add(tokens.get(logic.getCount()));
                        logic.increment(1);
                        instructionList.add(instruccion);
                    }
                }
    
                // Evaluar la condición
                Counter localCounter = new Counter();
                executeKeyWords(condition, localCounter);
                Boolean doExecute = localCounter.isValueBool();
    
                if(doExecute){
                    // Ejecutar cada instrucción por separado
                    for(ArrayList<String> instruccion : instructionList){
                        Counter c = new Counter();
                        c.setCount(0);
                        executeKeyWords(instruccion, c);
                    }
                    
                    logic.increment(1); // avanzar al cierre de parentesis

                    parenthesisCount =1;
                    while(parenthesisCount!=-1){
                        if(tokens.get(logic.getCount()+1).equals("(")){
                            parenthesisCount++;
                        }else if(tokens.get(logic.getCount()+1).equals(")")){
                            parenthesisCount--;
                        }

                        logic.increment(1);
                    }
            
                    logic.increment(-1); //colocar en el token anterior para el funcionamiento de execute
                    return logic;

                } else {
                    //si no se ha terminado el cond, hacer recursividad con las demas condiciones
                    if(!tokens.get(logic.getCount()+1).equals(")")){
                        logic.increment(-1);
                        return cond(tokens, logic);
                    }
                    //si no se cumple y no hay otras condiciones avanza a la siguiente instruccion
                    else {
                        return logic; 
                    }
                }
                
            } else {
                exitForErrorSintax(7);
            }

        //cuando encuentra t siempre se ejecutan las instrucciones siguientes
        } else if(tokens.get(logic.getCount()).equals("(") && tokens.get(logic.getCount()+1).equals("t")){
            //verificar estructura
            if(tokens.get(logic.getCount()+2).equals("(")){
                logic.increment(2);

                //Separar cada instruccion y meterlas en una lista
                ArrayList<ArrayList<String>> instructionList = new ArrayList<>();
                while(!tokens.get(logic.getCount()).equals(")")){
                    if(tokens.get(logic.getCount()).equals("(")){
                        ArrayList<String> instruccion = new ArrayList<>();
                        int parenthesisCounter = 1;
                        instruccion.add(tokens.get(logic.getCount()));
                        logic.increment(1);

                        while(parenthesisCounter != 0){
                            String token = tokens.get(logic.getCount());
                            if(token.equals("(")){
                                parenthesisCounter++;
                            } else if(token.equals(")")){
                                parenthesisCounter--;
                            }
                            instruccion.add(token);
                            logic.increment(1);
                        }
                        instructionList.add(instruccion);

                    //tokens sueltos
                    } else {
                        ArrayList<String> instruccion = new ArrayList<>();
                        instruccion.add(tokens.get(logic.getCount()));
                        logic.increment(1);
                        instructionList.add(instruccion);
                    }
                }
    
                // Ejecutar cada instrucción
                for(ArrayList<String> instruction : instructionList){
                    Counter c = new Counter();
                    c.setCount(0);
                    executeKeyWords(instruction, c);
                }
                return logic;
    
            } else {
                exitForErrorSintax(-1);
            }
        } else {
            exitForErrorSintax(-1);
        }
        return logic;
    }
}