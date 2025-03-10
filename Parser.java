
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    List<String> KEYWORDS = new ArrayList<>(Arrays.asList(
        "<", ">", "+", "-", "*", "/", "^", "(", ")", "'","=", 
        "defun", "quote", "setq", "cond", "atom", "list", "equal"
    ));

    ArrayList<String> tokens = new ArrayList<>();
    ArrayList<Defun> functions = new ArrayList<>();
    ArrayList<HashMap> variables = new ArrayList<>();


    
    
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
                    ArrayList<Object> parametrizacion = new ArrayList<>();
                    parametrizacion.add(i);
                    i = (Integer) executeKeyWords(tokens, parametrizacion).get(0);
                }else{
                    exitForErrorSintax(3);
                }
            }else{
                exitForErrorSintax(1);
            }
        }
    }

    public ArrayList<Object> executeKeyWords(ArrayList<String> tokens, ArrayList<Object> parametrizacion){
        int  i=(Integer) parametrizacion.get(0);
        //get(0) = i int
        //get(1) = return double
        //get(2) = return boolean
        //get(3) = String
        //get(4) = ArrayList(Strings)
        //get(5) = variablesLocal
        
        if(i>=tokens.size()){
            System.out.println("Error: Fin del programa");
            System.exit(0);
        }
        switch (tokens.get(i + 1)) {
            case "setq":
                setq(tokens, i, null);
            case "quote":
                
            case "cond":
                
            case "atom":
            case "list":

            case "equal":
            case "<":
            case ">":
                i = (Integer) predicates(tokens, parametrizacion).get(0);
            case "+":
            case "-":
            case "*":
            case "/":
        
            default:
                break;
        }
        parametrizacion.set(0, i);
        return parametrizacion;
    }

    public int setq(ArrayList<String> tokens, int i,ArrayList<String> variablesLocal){
        i=i+2;
        while(true){
            if (tokens.get(i)==")"){
                i++;
                return i;
            }else if(KEYWORDS.contains(tokens.get(i))){
                
            }
        }
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

    public ArrayList<Object> predicates(ArrayList<String> tokens, ArrayList<Object> parametrizacion){
        int  i=(Integer) parametrizacion.get(0);

        String operator = tokens.get(i + 1);
        String value1="", value2 = "";
        if (isNumber(tokens.get(i + 2))) {
            value1 = tokens.get(i + 2);
        // }else if(searchInHashMaps(value1, variablesLocal)!=null){
        //     value1 = searchInHashMaps(value1, variablesLocal);
         }
        if (isNumber(tokens.get(i + 3))) {
            value2 = tokens.get(i + 3);
        // }else if(searchInHashMaps(value2, variablesLocal)!=null){
        //     value2 = searchInHashMaps(value1, variablesLocal);
         }
        Predicate predicate = new Predicate();
        System.out.println(predicate.evaluate(operator, value1, value2));
        if(!tokens.get(i+4).equals(")"))
            exitForErrorSintax(2);
        i=i+5;
        parametrizacion.set(0, i);
        return parametrizacion;
    }

    public static String searchInHashMaps(String key, ArrayList<HashMap<String, String>> hashMapList) {
        for (HashMap<String, String> map : hashMapList) {
            if (map.containsKey(key)) {
                return map.get(key);
            }
        }
        return null;  // No se encontró la clave en ninguno de los HashMaps
    }
}