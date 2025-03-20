
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
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
            case 5:
                System.out.println("Error: Al no encontrar valores para operar");
                break;
            case 6: 
                System.out.println("Error: Numero de parametros incorrecto");
                break;
            case 7: 
                System.out.println("Error: Fin del programa");
                break;
        
            default:
                System.out.println("Error: Sintaxis de Lisp incorrecta");
                break;
        }
        System.exit(0);
    }

    int globalEnviroment = 0;
    int depth = 0;

    public void execute(ArrayList<String> tokens) {
        for(int i=0; i<tokens.size(); i++){
            
            if(tokens.get(i).equals("(")&& globalEnviroment==0){
                globalEnviroment=1;
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
                        globalEnviroment=0;
                    }
                }else if(KEYWORDS.contains(tokens.get(i+1))){
                    depth++;
                    globalEnviroment=0;
                    Counter counterGlobal = new Counter();
                    counterGlobal.setCount(i);
                    counterGlobal=executeKeyWords(tokens, counterGlobal);                
                    i=counterGlobal.getCount();
                    
                }else{
                    exitForErrorSintax(3);
                }
            }else if(i>=tokens.size()){
                exitForErrorSintax(7);

            }else{
                exitForErrorSintax(1);
            }
        }
    }

    public Counter executeKeyWords(ArrayList<String> tokens, Counter logic){
        
        if(logic.getCount()>=tokens.size()){
            exitForErrorSintax(7);
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

            //error en caso de numero invalido de parametros
            if(parametrosSend.size()!=defunRun.getParametros().size()){
                exitForErrorSintax(6);
            }            
            // Avanzamos el contador para saltar el paréntesis de cierre
            Counter counterDefun = new Counter();
            logic = defun(defunRun, counterDefun,parametrosSend);
            System.out.println("funcion finalizada");
            
        }else{
            switch (tokens.get(logic.getCount() + 1)) {
                case "setq":
                    logic =setq(tokens, logic);
                    break;
                    
                case "quote":

                    logic = quote(tokens, logic);
                    depth--;
                    break;
                    
                case "cond":
                    
                case "atom":
                    logic = atom(tokens, logic);
                    break;
                case "list":
                    logic = list(tokens, logic);
                    break;

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
        logic.setValue(quote.toAtom());
        if (depth == 1) {
            System.out.println(quote.toString());
        }

        if (depth == 0) {
            System.out.println(quote.toAtom());
        }
        logic.increment(i - logic.getCount());
        return logic;
    }

    public Counter atom(ArrayList<String> tokens, Counter logic) {
        int i = logic.getCount() + 2;  
        Counter result = new Counter();
        Quote atom = new Quote();
        StringBuilder atomContent = new StringBuilder();


        // encentra un parentesis abierto
        if (tokens.get(i).equals("(")) {
            depth++;
            result.setCount(i);
            result = executeKeyWords(tokens, result);
            atomContent.append(result.getValue());
        }
        
        // si coincide con varialbles
        else if (variables.getValue(tokens.get(i)) != null) {
            atomContent.append(variables.getValue(tokens.get(i)));
            result.setCount(i);
            
        // si es un número
        } else if (isNumber(tokens.get(i)) || variables.getValue(tokens.get(i)) != null) {
            
            atomContent.append(tokens.get(i));
            result.setCount(i); 
        // cualquier otro caso
        } else {
            
            atomContent.append(tokens.get(i));
            result.setCount(i);
        } 

        // Verificar si el contenido es un átomo y retornar el resultado
        
        atom.setExpresion(atomContent);
        System.out.println(atom.isAtom());
        logic.setCount(result.getCount() + 1);
        depth--;
        return logic;

    }

    public Counter list (ArrayList<String> tokens, Counter logic){
        depth--;
        quote(tokens, logic);
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
                 }
                
             }

            //validar variables
            if(variables.getValue(value)!=null){
                value=variables.getValue(value);
            }

            
            logic.increment(1);;

    
            // Asignar la variable y su valor
            if(globalEnviroment==0){
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

    public Counter defun(Defun defunRun, Counter logicD, ArrayList<String> parametros){
        globalEnviroment++; 
        ArrayList<String> tokensD = defunRun.getCuerpo();
        //aregar parametros como variables locles
        for(int j=0; j<parametros.size(); j++){
            defunRun.addVariable(defunRun.getParametros().get(j),parametros.get(j));
        }
        System.out.println("funcion interns");
        while(logicD.getCount()<tokensD.size()){
            System.out.println(tokensD.get(logicD.getCount()));
            System.out.println(logicD.getCount());
            //manejar funcion interna
            if (!tokensD.get(logicD.getCount()).equals("(")){
                exitForErrorSintax(1);
            }else if(KEYWORDS.contains(tokensD.get(logicD.getCount()+1))){
                System.out.println("aaaaa");
                System.out.println(tokensD.get(logicD.getCount())+" "+tokensD.get(logicD.getCount()));
                logicD=executeKeyWords(tokensD, logicD);  
                logicD.increment(1);
            }
        }
        globalEnviroment--; 
        return logicD;

    } 
    
}