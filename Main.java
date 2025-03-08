import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        ArrayList<Node> instrucciones = new ArrayList<>();
        ArrayList<HashMap> variables = new ArrayList<>();
        
        Parser parser = new Parser();
        String code = readFile("codigo.txt");
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

    public static String readFile(String path) {
        String codeText="";
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                codeText += line;
            }
        } catch (IOException e) {
            System.out.println("Error en lectura del documento: " + e.getMessage());
        }
        System.err.println(codeText);
        return codeText;
    }
}





//detectar un texto "" en tokenizador y no agregar 6
//detectar texto como - _ y otros valores en funciones 6
// validar que la siguiente letra y no quedarse solo si lo encuentra 9