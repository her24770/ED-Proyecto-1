
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
 * Clase que se encarga de tokenizar y ejecutar el código de Lisp
 * 
 */

public class Parser {

    // Lista de palabras clave

    List<String> KEYWORDS = new ArrayList<>(Arrays.asList(
        "<", ">", "+", "-", "*", "/", "^", "(", ")", "'","=", 
        "defun", "quote", "setq", "cond", "atom", "list", "equal"
    ));

    // Lista de tokens del código
    ArrayList<String> tokens = new ArrayList<>();
    // Lista de funciones globales
    ArrayList<Defun> functions = new ArrayList<>();
    // Conjunto de variables globales
    SetQ variables = new SetQ();


    
    
    /**
     * Algoritmo de balanceo de paréntesis
     * Verifica si un código tiene la misma cantidad de paréntesis de apertura y cierre
     * @param code código a verificar
     * @return true si está balanceado, false si no
     */
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

    /**
     * Función para tokenizar un código de Lisp
     * @param code código a tokenizar
     * @return lista con los tokens
     */

    public ArrayList<String> tokenize(String code) {

        ArrayList<String> tokens = new ArrayList<>();

        Pattern pattern = Pattern.compile("\\(|\\)|'|\\w+|[-+*/^<>=]");
        Matcher matcher = pattern.matcher(code);

        while (matcher.find()) {
            tokens.add(matcher.group());
        }

        return tokens;
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

    /**
     * Función para manejar errores de sintaxis
     * @param typeError tipo de error
     */
    
    
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
            case 8: 
                System.out.println("Error: Funcion ya declarada con el mismo nombre");
                break;
        
            default:
                System.out.println("Error: Sintaxis de Lisp incorrecta");
                break;
        }
        System.exit(0);
    }

    // Variable para controlar el entorno global

    int globalEnviroment = 0;


    /**
     * Función para ejecutar el código de Lisp
     * @param tokens lista de tokens
     */

    public void execute(ArrayList<String> tokens) {
        for(int i=0; i<tokens.size(); i++){
            // Verificar si es una función y definir fuera del entorno global
            if(tokens.get(i).equals("(")&& globalEnviroment==0){
                globalEnviroment=1;
                if(tokens.get(i+1).equals("defun")){
                    Defun defunNew = new Defun();
                    if(searchDefun(functions, tokens.get(i+2))!=null){
                        exitForErrorSintax(8);
                    }else{
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
                    }
                    
                        globalEnviroment=0;
                    }

                // si no es una funcion, ejecutar los keywords
                }else if(KEYWORDS.contains(tokens.get(i+1)) || searchDefun(functions, tokens.get(i+1))!=null ){
                    globalEnviroment=0;
                    Counter counterGlobal = new Counter();
                    counterGlobal.setCount(i);
                    counterGlobal=executeKeyWords(tokens, counterGlobal);            
                    i=counterGlobal.getCount();

                // No se encontró una función o keyword
                }else{
                    exitForErrorSintax(3);
                }
            // Ya se ejecuto todo el código
            }else if(i>=tokens.size()){
                exitForErrorSintax(7);
            // No se encontró un paréntesis de apertura
            }else{
                exitForErrorSintax(1); 
            }
        }
    }


    /**
     * Función para ejecutar los keywords de la lista de palabras clave
     * @param tokens lista de tokens
     * @param logic contador de tokens y logística
     * @return contador actualizado con valor y posición
     */

    public Counter executeKeyWords(ArrayList<String> tokens, Counter logic){

        // Verificar si se ha llegado al final del código
        
        if(logic.getCount()>=tokens.size()){
            exitForErrorSintax(7);
        }

        // Verificar si es una función
        if (searchDefun(functions, tokens.get(logic.getCount()+1))!=null){
            logic.increment(1);
            
            // ArrayList para guardar los parámetros
            ArrayList<String> parametrosSend = new ArrayList<>();
            Defun defunRun =searchDefun(functions, tokens.get(logic.getCount()));
            logic.increment(1);
            // Leemos los tokens hasta encontrar un paréntesis de cierre
            while (!tokens.get(logic.getCount()).equals(")")) {
                // Validar los parametros y setearlos como variables
                if (logic.getVairablesLocales().getValue(tokens.get(logic.getCount()))!=null && globalEnviroment!=0){
                    parametrosSend.add(logic.getVairablesLocales().getValue(tokens.get(logic.getCount())));
                }else if(variables.getValue(tokens.get(logic.getCount()))!=null){
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
            
            logic = defun(defunRun, logic,parametrosSend);
            
            System.out.println("funcion finalizada");

        // Si no es una función, verificar si es un keyword
            
        }else{
            switch (tokens.get(logic.getCount() + 1)) {
                case "setq":
                    logic =setq(tokens, logic);
                    break;

                
                case "'":    
                case "quote":

                    logic = quote(tokens, logic);
                    break;
                    
                case "cond":
                    logic = cond(tokens, logic);
                    logic.increment(1);
                    System.out.println("valor retorno cont  "+logic.getValue());
                    break;
                case "atom":
                    logic=atom(tokens, logic);
                    break;

                case "list":
                    logic=list(tokens, logic);
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
                    System.out.println(logic.getValue());
                    break;
            
                default:
                    break;
            }
        }
        return logic;
    }

    /**
     * Funcion para definir un QUOTE.
     * @param tokens lista de tokens
     * @param logic contador de tokens y logistica
     * @return contador actualizado con valor y posición
     */

    public Counter quote(ArrayList<String> tokens, Counter logic) {
        // Crear un StringBuilder para guardar la expresión
        StringBuilder quoteBodyBuilder = new StringBuilder();
        // Crear un objeto Quote para guardar la expresión
        Quote quote = new Quote();
        // Avanzar el contador para saltar el paréntesis de apertura
        int i = logic.getCount() + 2; 

        // Verificar si el QUOTE tiene argumentos
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
        // Si no tiene argumentos, mostrar error    
        } else {
            System.out.println("Error: quote sin argumento");    
        }
        // Asignar la expresión al objeto Quote
        quote.setExpresion(quoteBodyBuilder);
        logic.setValue(quote.toAtom());
        logic.increment(i - logic.getCount());
        return logic;
    }


    /**
     * Función para definir un Atomo
     * @param tokens lista de tokens
     * @param logic contador de tokens y logistica
     * @return contador actualizado con valor y posición
     */

    public Counter atom(ArrayList<String> tokens, Counter logic) {

        // Avanzar el contador para saltar el paréntesis de apertura
        int i = logic.getCount() + 2;  
        Counter result = new Counter();
        Quote atom = new Quote();
        StringBuilder atomContent = new StringBuilder();
        
        if (tokens.get(i).equals("(" ) && searchDefun(functions, tokens.get(i+1)) == null) {

            if (KEYWORDS.contains(tokens.get(i+1))) {
                result.setCount(i);
                result = executeKeyWords(tokens, result);
                atomContent.append(result.getValue());
                i = result.getCount() + 1;
            }
            else {

                atomContent.append(tokens.get(i)).append(" ");

                int parentesisHandler = 1; 
                i++;
                
                while (parentesisHandler > 0)  {
                    String token = tokens.get(i);
                    if (token.equals("(")){
                        parentesisHandler++;
                    }      
                    else if (token.equals(")")){
                        parentesisHandler--;
                    }
                    
                    atomContent.append(token).append(" ");
                    i++;
                }
            }

        // si es una variable 
        }else if (logic.getVairablesLocales().getValue(tokens.get(i))!=null&&globalEnviroment!=0){
            atomContent.append(logic.getVairablesLocales().getValue(tokens.get(i)));
            i++;

        // si es una variable global
        }else if (variables.getValue(tokens.get(i))!=null){
            atomContent.append(variables.getValue(tokens.get(i)));
            i++;

        }

        // es una funcion
        else if (tokens.get(i).equals("(") && searchDefun(functions, tokens.get(i+1))!=null){
            StringBuilder functionContent = new StringBuilder();

            logic.setCount(i);
                    logic = executeKeyWords(tokens, logic);
                    functionContent.append(logic.getValue());
                    i = logic.getCount() + 1;
                    logic.increment(1);

            atomContent.append(functionContent);

        }

        //asignar el contenido al objeto quote

        atom.setExpresion(atomContent);
        System.out.println(atom.isAtom());
        logic.setValueBool(atom.isAtom());
        logic.increment(i - logic.getCount());

        return logic;

    }

    /**
     * Función para definir un List
     * @param tokens lista de tokens
     * @param logic contador de tokens y logistica
     * @return contador actualizado con valor y posición
     */

    public Counter list(ArrayList<String> tokens, Counter logic){
        // Avanzar el contador para saltar el paréntesis de apertura
        int i = logic.getCount() + 2;  
        Counter result = new Counter();
        Quote atom = new Quote();
        StringBuilder atomContent = new StringBuilder();



        if (tokens.get(i).equals("(" ) && searchDefun(functions, tokens.get(i+1)) == null) {

            if (KEYWORDS.contains(tokens.get(i+1))) {
                
                result.setCount(i);
                result = executeKeyWords(tokens, result);
                atomContent.append(result.getValue());
                i = result.getCount() + 1;
                
            }
            else {

                atomContent.append(tokens.get(i)).append(" ");

                int parentesisHandler = 1; 
                i++;
                
                while (parentesisHandler > 0)  {
                    String token = tokens.get(i);
                    if (token.equals("(")){
                        parentesisHandler++;
                    }      
                    else if (token.equals(")")){
                        parentesisHandler--;
                    }
                    
                    atomContent.append(token).append(" ");
                    i++;
                }
                
            }
            // si es una variable
        }else if (logic.getVairablesLocales().getValue(tokens.get(i))!=null&&globalEnviroment!=0){
            atomContent.append(logic.getVairablesLocales().getValue(tokens.get(i)));
            i++;
            // si es una variable global
        }else if (variables.getValue(tokens.get(i))!=null){
            atomContent.append(variables.getValue(tokens.get(i)));
            i++;

        }
        // es una funcion
        else if (tokens.get(i).equals("(") && searchDefun(functions, tokens.get(i+1))!=null){
            
            StringBuilder functionContent = new StringBuilder();

            logic.setCount(i);
                    logic = executeKeyWords(tokens, logic);
                    functionContent.append(logic.getValue());
                    i = logic.getCount() + 1;
                    logic.increment(1);

            atomContent.append(functionContent);

        }

        //Asignar el contenido al objeto quote
        
        atom.setExpresion(atomContent);
        System.out.println(atom.isList());
        logic.setValueBool(atom.isList());
        logic.increment(i - logic.getCount());

        return logic;
    }


    /**
     * metodo para definir una operacion aritmetica  
     * @param tokens lista de tokens
     * @param logic contador de tokens y logistica
     * @return contador actualizado con valor y posición
     */

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
                Defun defunsearch = searchDefun(functions, tokens.get(i+1));
                if (defunsearch!=null){                    
                    logic.setCount(i);
                    logic = executeKeyWords(tokens, logic);
                    values.add(logic.getValue());
                    i = logic.getCount()+1;
                    logic.increment(1);
                }
                else if(tokens.get(i+1).equals("quote") || tokens.get(i+1).equals("'")){
                    logic.setCount(i);
                    logic = quote(tokens, logic);
                    values.add(logic.getValue());
                    i=logic.getCount()+1;

                }
                else{
                    Counter counter = new Counter();
                    counter.setCount(i);
                    values.add(String.valueOf(arithmeticOperation(tokens, counter).getValue()));
                    i=counter.getCount()+1;
                }
                
            }else if (logic.getVairablesLocales().getValue(tokens.get(i))!=null&&globalEnviroment!=0){
                values.add(logic.getVairablesLocales().getValue(tokens.get(i)));
                i++;
            }else if (variables.getValue(tokens.get(i))!=null){
                values.add(variables.getValue(tokens.get(i)));
                i++;
            }
            else if(!tokens.get(i).equals(")") && tokens.get(logic.getCount()).equals(")")){
                exitForErrorSintax(5); // Manejo de error sintáctico
            }
            else{
                exitForErrorSintax(-1);
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
        System.out.println("reultado suma: "+result);
        return logic;
    }

    /**
     * metodo para definir un predicado
     * @param tokens lista de tokens
     * @param logic contador de tokens y logistica
     * @return contador actualizado con valor y posición
     */


    public Counter predicates(ArrayList<String> tokens, Counter logic) {
        String operator = tokens.get(logic.getCount() + 1);
        List<String> values = new ArrayList<>();

        // Recopilar todos los valores hasta encontrar ")"
        logic.increment(2);
        while (logic.getCount() < tokens.size() && !tokens.get(logic.getCount() ).equals(")")) {
            if (isNumber(tokens.get(logic.getCount() ))) {
                values.add(tokens.get(logic.getCount() ));
            }else if (logic.getVairablesLocales().getValue(tokens.get(logic.getCount()))!=null && globalEnviroment!=0){
                values.add(logic.getVairablesLocales().getValue(tokens.get(logic.getCount())));
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
        return logic;
    }

    /**
     * Función para definir un SETQ
     * @param tokens lista de tokens
     * @param logic contador de tokens y logística
     * @return contador actualizado con valor y posición
     */

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
                if(KEYWORDS.contains(tokens.get(logic.getCount()+1))){
                    logic = executeKeyWords(tokens, logic);
                    value=logic.getValue();
                }else if (defunsearch!=null){
                    logic = executeKeyWords(tokens, logic);
                    value=logic.getValue();
                }
                
            }

            //validar variables
            if(globalEnviroment!=0){
                if(logic.getVairablesLocales().getValue(value)!=null){
                    value=logic.getVairablesLocales().getValue(value);
                }else if(variables.getValue(value)!=null){
                    value=variables.getValue(value);
                
                }
            }else{
                if(variables.getValue(value)!=null){
                    value=variables.getValue(value);
                }
            }

            

            logic.increment(1);;    
            // Asignar la variable y su valor
            if(globalEnviroment==0){
                variables.assign(variable, value);
            }else{
                logic.addVariableLocal(variable, value);
            }
        }
        
    
        // Verificar el cierre ")"
        if (logic.getCount() >= tokens.size() || !tokens.get(logic.getCount()).equals(")")) {
            exitForErrorSintax(4); // Manejo de error sintáctico
        }
    
        // Opcional: Imprimir las variables para debugging
        System.out.println("Variables:");
        variables.printVariables();
        System.out.println("Variables locales:");
        logic.getVairablesLocales().printVariables();
    
        return logic;
    }

    /**
     * Función para buscar una función en la lista de funciones
     * @param functions lista de funciones
     * @param nombre nombre de la función a buscar
     * @return función encontrada
     */

    public Defun searchDefun(ArrayList<Defun> functions, String nombre) {
        for (Defun defun : functions) {
            if(defun.getNombre().equals(nombre)) {
                return defun;
            }
        }
        return null;
    }

    /**
     * Función para definir una función
     * @param defunRun función a definir
     * @param logic contador de tokens y logística
     * @param parametros lista de parámetros
     * @return contador actualizado con valor y posición
     */

    public Counter defun(Defun defunRun, Counter logic, ArrayList<String> parametros){
        globalEnviroment++; 
        Counter logicD = new Counter();
        for (int j = 0; j < parametros.size(); j++) {
            
            logicD.addVariableLocal(defunRun.getParametros().get(j), parametros.get(j));
        }
        

        ArrayList<String> tokensD = defunRun.getCuerpo();
        //aregar parametros como variables locles
        for(int j=0; j<parametros.size(); j++){
            defunRun.addVariable(defunRun.getParametros().get(j),parametros.get(j));
        }
        System.out.println("funcion interns");
        while(logicD.getCount()<tokensD.size()){
            System.out.println(logicD.getCount()+""+tokensD.get(logicD.getCount()+1));

            //manejar funcion interna
            if (!tokensD.get(logicD.getCount()).equals("(")){
                exitForErrorSintax(1);
            }else if(KEYWORDS.contains(tokensD.get(logicD.getCount()+1))|| searchDefun(functions, tokensD.get(logicD.getCount()+1))!=null){
                System.out.println(tokensD.get(logicD.getCount())+ " counter :"+logicD.getCount());
                logicD=executeKeyWords(tokensD, logicD);  
                logicD.increment(1);
            }
        }
        logic.setValue(logicD.getValue());
        globalEnviroment--; 
        return logic;

    } 

    /**
     * Función para evaluar la estructura de COND
     * @param tokens codigo a evaluar
     * @param logic contador de tokens y logistica
     * @return contador actualizado con valor y posición
     */
    public Counter cond(ArrayList<String> tokens, Counter logic){
        Counter value = new Counter();
        value.setVairablesLocales(logic.getVairablesLocales());
        logic.increment(2); // Saltar lo verificado en executeKeyWords
    
        // Verificar estructura de COND (doble paréntesis)
        if(tokens.get(logic.getCount()).equals("(") && tokens.get(logic.getCount()+1).equals("(")){
            logic.increment(1);
    
            // Verificar si es una condición válida
            if(!tokens.get(logic.getCount()+1).equals(")") && !tokens.get(logic.getCount()+1).equals("(") &&
               !tokens.get(logic.getCount()+1).equals("setq") && !tokens.get(logic.getCount()+1).equals("defun") &&
               !tokens.get(logic.getCount()+1).equals("cond") && !tokens.get(logic.getCount()+1).equals("quote") && !tokens.get(logic.getCount()+1).equals("t")){
                
                // Verifica si el comparador es válido
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
    
                // Separar cada instrucción y meterlas en una lista
                ArrayList<ArrayList<String>> instructionList = new ArrayList<>();
                while(logic.getCount() < tokens.size() && !tokens.get(logic.getCount()).equals(")")){
                    if(tokens.get(logic.getCount()).equals("(")){
                        ArrayList<String> instruction = new ArrayList<>();
                        int parenthesisCounter = 1;
                        instruction.add(tokens.get(logic.getCount()));
                        logic.increment(1);
    
                        while(parenthesisCounter != 0 && logic.getCount() < tokens.size()){
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
                    // Para agregar los tokens fuera de paréntesis
                    else {
                        ArrayList<String> instruccion = new ArrayList<>();
                        instruccion.add(tokens.get(logic.getCount()));
                        logic.increment(1);
                        instructionList.add(instruccion);
                        System.out.println(instructionList);
                    }
                }
    
                // Evaluar la condición
                Counter localCounter = new Counter();
                
                localCounter.setVairablesLocales(logic.getVairablesLocales());
                executeKeyWords(condition, localCounter);

                Boolean doExecute = localCounter.isValueBool();
    
                if(doExecute){
                    // Ejecutar cada instrucción por separado
                    if(instructionList.size() != 0){

                        if(instructionList.getLast().size() == 1){
                            String token = instructionList.getLast().get(0);
                            System.out.println(token);
                            if(isNumber(token)){
                                value.setValue(token);

                            }else if(globalEnviroment!=0){
                                if(logic.getVairablesLocales().getValue(token)!=null){
                                    value.setValue(logic.getVairablesLocales().getValue(token));
                                }else if(variables.getValue(token)!=null){
                                    value.setValue(variables.getValue(token));
                                }else{
                                    value.setValue(token);
                                }
                            }else if (globalEnviroment==0){
                                if(variables.getValue(token)!=null){
                                    value.setValue(variables.getValue(token));
                                }else{
                                    value.setValue(token);  
                                }
                            }
                            
                        }else{
                            for(ArrayList<String> instruction : instructionList){
                                Counter c = new Counter();
                                c.setVairablesLocales(logic.getVairablesLocales());
                                value = executeKeyWords(instruction, c);
                                System.out.println("valor retorno xd : "+value.getValue());  
                            }

                        }
                    }
    
                    logic.increment(1); // Avanzar al cierre de paréntesis
    
                    parenthesisCount = 1;
                    while(parenthesisCount != -1 && logic.getCount() < tokens.size()){
                        if(tokens.get(logic.getCount()+1).equals("(")){
                            parenthesisCount++;
                        }else if(tokens.get(logic.getCount()+1).equals(")")){
                            parenthesisCount--;
                        }
                        logic.increment(1);
                    }
                    logic.setValue(value.getValue()); // Colocar en el token anterior para el funcionamiento de execute
                    return logic;
    
                } else {
                    // Si no se ha terminado el cond, hacer recursividad con las demás condiciones
                    if(logic.getCount() < tokens.size() && !tokens.get(logic.getCount()+1).equals(")")){
                        logic.increment(-1);
                        return cond(tokens, logic);
                    }
                    // Si no se cumple y no hay otras condiciones, avanza a la siguiente instrucción
                    else {
                        return logic; 
                    }
                }
                
            } else {
                exitForErrorSintax(7);
            }
    
        // Cuando encuentra t, siempre se ejecutan las instrucciones siguientes
        } else if(tokens.get(logic.getCount()).equals("(") && tokens.get(logic.getCount()+1).equals("t")){
            // Verificar estructura
            if(tokens.get(logic.getCount()+2).equals("(")){
                logic.increment(2);
    
                // Separar cada instrucción y meterlas en una lista
                ArrayList<ArrayList<String>> instructionList = new ArrayList<>();
                while(logic.getCount() < tokens.size() && !tokens.get(logic.getCount()).equals(")")){
                    if(tokens.get(logic.getCount()).equals("(")){
                        ArrayList<String> instruccion = new ArrayList<>();
                        int parenthesisCounter = 1;
                        instruccion.add(tokens.get(logic.getCount()));
                        logic.increment(1);
    
                        while(parenthesisCounter != 0 && logic.getCount() < tokens.size()){
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
                    // Para tokens sueltos
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
                    c.setVairablesLocales(logic.getVairablesLocales());
                    value = executeKeyWords(instruction, c);
                }
                logic.setValue(value.getValue());
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