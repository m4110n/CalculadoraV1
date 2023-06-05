package GENERAL;

import java.io.BufferedReader; // Clase para leer archivos de texto
import java.io.BufferedWriter; // Clase para escribir en archivos de texto
import java.io.FileReader; // Clase para leer archivos de texto
import java.io.FileWriter; // Clase para escribir en archivos de texto
import java.io.IOException; // Clase para manejar excepciones de entrada/salida


//Lee la entrada y salida del archivo donde se almacena la informacion
public class Main {
    public static void main(String[] args) {
        String archivoEntrada = "files/input.txt"; 
        String archivoSalida = "files/output.txt"; 
        AnalizadorLexico analizadorLexico = new AnalizadorLexico(); // Instancia del analizador léxico
        
//abre el archivo de entrada para leer y abre el archivo de salida para escribir
        try (BufferedReader br = new BufferedReader(new FileReader(archivoEntrada)); 
             BufferedWriter bw = new BufferedWriter(new FileWriter(archivoSalida))) { 
// y vacia el archivo de salida
            bw.write(""); // Vacía el contenido del archivo de salida

//en este proceso va leendo cada linea del archivo elimiando los signos iguales, los espacios en blanco de la linea y realiza el analisis lexico
            String linea;
            while ((linea = br.readLine()) != null) { // Lee cada línea del archivo de entrada
                linea = linea.replace("=", "").replace(" ", ""); // Elimina el signo igual y los espacios en blanco de la línea
                String resultado = analizadorLexico.generarTokens(linea); // Realiza el análisis léxico de la línea
                
//aqui crea la cadena del resultado final y va escribiendo cada resultado he insertando una nueva linea de archivo de salida
                String resultadoFinal = linea + " = " + resultado; // Crea la cadena con el resultado final
                bw.write(resultadoFinal); // Escribe el resultado en el archivo de salida
                bw.newLine(); // Inserta una nueva línea en el archivo de salida
            }
//captura todos los errores del catch y muestra solo un mensaje
        } catch (IOException e) { // Captura cualquier excepción de entrada/salida
            e.printStackTrace(); // Imprime el rastreo de la excepción
        }
    }
}
