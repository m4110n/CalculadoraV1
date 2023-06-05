package GENERAL;

import java.util.ArrayList;

public class AnalizadorLexico {
    
    // Método para identificar tokens 
    public String generarTokens(String operacion) {
        
        // Instancia que va leendo caracter por caracter si tiene sentido como los numeros y los operadores 
        ArrayList<String> tokensNumeros = new ArrayList<>(); // Array para almacenar tokens numéricos
        ArrayList<String> tokensCaracteres = new ArrayList<>(); // Array para almacenar tokens de caracteres
        ArrayList<String> tokensOperadores = new ArrayList<>(); // Array para almacenar tokens de operadores

        // Extraer cada dato del arraylist y los guarda en una lista correspondiente
        for (int i = 0; i < operacion.length(); i++) {
            String token = String.valueOf(operacion.charAt(i));
            tokensCaracteres.add(token); // Agregar el token de caracteres al array correspondiente
        }

        // lee cada caracter y si es un numero lo guarda en la lista de numerico y si en caso no es un numerico detecta si el numero es mayor a 0 y si no es menor a 0 vuelve a empezar el bucle con otra linea
        StringBuilder numeros = new StringBuilder();
        for (int i = 0; i < operacion.length(); i++) {
            char caracter = operacion.charAt(i);
            if (Character.isDigit(caracter) || caracter == '.') {
                numeros.append(caracter); // Agregar caracteres numéricos o punto decimal al StringBuilder
            } else {
                if (numeros.length() > 0) {
                    tokensNumeros.add(numeros.toString()); // Agregar el token numérico al array correspondiente
                    numeros.setLength(0); // Reiniciar el StringBuilder para el próximo token numérico
                }
            }
        }
        
        // Verifica si queda un token numerico pendiente y si queda lo agrega al arraylist antes de continuar con el proceso
        if (numeros.length() > 0) {
            tokensNumeros.add(numeros.toString());
        }

        // Esta funcion va leendo caracter por caracter detectando si es un token numerico y token logico y los va almacenando en una lista
        StringBuilder operadores = new StringBuilder();
        for (int i = 0; i < operacion.length(); i++) {
            char caracter = operacion.charAt(i);
            if (!Character.isDigit(caracter) && caracter != '.') {
                if (operadores.length() > 0) {
                    tokensOperadores.add(operadores.toString()); // Agregar el token de operador al array correspondiente
                    operadores.setLength(0); // Reiniciar el StringBuilder para el próximo token de operador
                }
                tokensOperadores.add(String.valueOf(caracter)); // Agregar el token de operador al array correspondiente
            } else {
                operadores.append(caracter); // Agregar caracteres numéricos o punto decimal al StringBuilder
            }
        }
        
        // verifica que cada token numerico o caracter pendiente y los guarda en una lista
        if (operadores.length() > 0) {
            tokensOperadores.add(operadores.toString()); // Agregar el último token de operador al array correspondiente
        }
        
        // hace el llamado al analizador sintactico
        AnalizadorSintactico AnalizadorSintactico = new AnalizadorSintactico(); // Crear una instancia del AnalizadorSintactico
        String resultado = AnalizadorSintactico.generarSintaxis(tokensCaracteres, tokensNumeros, tokensOperadores); // Llamar al método generarSintaxis y pasar los arrays de tokens como argumentos
        
        return resultado; // Devolver el resultado obtenido del análisis sintáctico
    }
}
