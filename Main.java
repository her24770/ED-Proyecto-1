import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Universidad del Valle de Guatemala
 * Algoritmos y Estructura de Datos
 * Seccion: 30
 * Proyecto No.1 
 * Interprete del lenguaje de programacion LISP 
 * @author Josue Hernandez 24770, Oscar Rompich 24880, Gabriel Hidalgo 24939
 * Ultima edición: 20-03-2025
 */

public class Main {
    
    /**
     * Metodo principal que ejecuta el programa
     * @param args
     */ 
    public static void main(String[] args) {
        ArrayList<String> tokens = new ArrayList<>();
        
        Parser parser = new Parser();
        String code = readFile("codigo.txt");
        if(parser.cierreParentesis(code)){
            tokens = parser.tokenize(code);
            
        }else{
            System.out.println("Error: Parentesis no cerrados");
        }

        parser.execute(tokens);


    }

    /**
     * Metodo que lee un archivo de texto
     * @param path
     * @return String con el contenido del archivo
     */ 

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
        return codeText;
    }
}





//detectar un texto "" en tokenizador y no agregar 6
//detectar texto como - _ y otros valores en funciones 6
// validar que la siguiente letra y no quedarse solo si lo encuentra 9