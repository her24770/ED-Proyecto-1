import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {

    public List<String> KEYWORDS = new ArrayList<>(Arrays.asList(
        "<", ">", "+", "-", "*", "/", "^", "(", ")", "'","=", 
        "defun", "quote", "setq", "cond", "atom", "list", "equal"
    ));
    public List<String> getKEYWORDS() {
        return KEYWORDS;
    }


    public void setKEYWORDS(List<String> kEYWORDS) {
        KEYWORDS = kEYWORDS;
    }
    
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
                token = "";
            }
    
            // Detectar palabras clave completas
            boolean foundKeyword = false;
            for (String kw : KEYWORDS) {
                if (code.startsWith(kw, i)) {
                    if (!token.isEmpty()) {
                        tokens.add(token);
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
                    KEYWORDS.add(token);
                    token = "";
                }
            } else {
                token += c;
            }
        }
    
        if (!token.isEmpty()) {
            tokens.add(token);
            KEYWORDS.add(token);
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
}