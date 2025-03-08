import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        ArrayList<Node> instrucciones = new ArrayList<>();
        ArrayList<HashMap> variables = new ArrayList<>();
        
        Parser parser = new Parser();
        String code = "(defun hola(x) (+ 2 1 - x)) (setq(y 5)) (quote(y))";
        if(parser.cierreParentesis(code)){
            ArrayList<String> tokens = parser.tokenize(code);
            for (String token : tokens) {
                System.out.println(token);
            }
            System.out.println(parser.getKEYWORDS());
        }else{
            System.out.println("Error: Parentesis no cerrados");
        }
    }
}
