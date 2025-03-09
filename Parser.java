import java.lang.reflect.Array;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
        String token = "";
        
        for (int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);
            
            // Verificar si el token acumulado es una palabra clave antes de agregarlo
            if (!token.isEmpty() && KEYWORDS.contains(token)) {
                tokens.add(token);
                if(!isNumber(token)){
                    KEYWORDS.add(token);
                }
                token = "";
            }
    
            // Detectar palabras clave completas
            boolean foundKeyword = false;
            for (String kw : KEYWORDS) {
                if (code.startsWith(kw, i)) {
                    if (!token.isEmpty()) {
                        tokens.add(token);
                        if(!isNumber(token)){
                            KEYWORDS.add(token);
                        }
                        token = "";
                    }
                    tokens.add(kw);
                    i += kw.length() - 1; // Avanza hasta el final de la palabra clave
                    foundKeyword = true;
                    break;
                }
            }
            if (foundKeyword) continue;
    
            if (c == ' ') {
                if (!token.isEmpty()) {
                    tokens.add(token);
                    if (!isNumber(token)) {
                        KEYWORDS.add(token);
                    }
                    token = "";
                }
            } else {
                token += c;
            }
        }
    
        if (!token.isEmpty()) {
            tokens.add(token);
            if(!isNumber(token)){
                KEYWORDS.add(token);
            }
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
        
            default:
                System.out.println("Error: Sintaxis de Lisp incorrecta");
                break;
        }
        System.exit(0);
    }

    boolean globalEnviroment = true;

    public void execute(ArrayList<String> tokens) {
        for(int i=0; i<tokens.size(); i++){
            System.out.println("for mayor"+tokens.get(i));
            if(tokens.get(i).equals("(")&& globalEnviroment==true){
                System.out.println("prueba execute");
                System.out.println(tokens.get(i));
                System.out.println(tokens.get(i+1));
                globalEnviroment=false;
                if(tokens.get(i+1)=="defun"){
                    Defun defunNew = new Defun();
                    defunNew.setNombre(tokens.get(i+2));
                    if(tokens.get(i+3)=="("){
                        i=i+4;
                        ArrayList<String> parametros = new ArrayList<>();
                        while(tokens.get(i)!=")"){
                            System.out.println(tokens.get(i));
                            parametros.add(tokens.get(i));
                            i++;
                        }
                        ArrayList<String> cuerpo = new ArrayList<>();
                        int contadorBrackets = 0;
                        i++;
                        while(contadorBrackets>=0){
                            System.out.println("prueva contenido defun");
                            System.out.println(tokens.get(i));
                            if(tokens.get(i)=="("){
                                contadorBrackets++;
                            }else if(tokens.get(i)==")"){
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

                
                
                }
            }else{
                exitForErrorSintax(1);
            }
        }
        for(Defun defun:functions){
            System.out.println(defun.getNombre());
            System.out.println(defun.getParametros());  
            System.out.println(defun.getCuerpo());
        }
    }

}