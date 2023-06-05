package GENERAL;

import java.util.ArrayList;
import java.util.Stack;

public class AnalizadorSemantico {

    //Metodo que opera los tokens validos
    public String recibirTokens(ArrayList<String> tokens) {
        // Manda array para obtener resultado
        double resultado = evaluarExpresion(tokens);
        String resultadoText = "";
        
        //Determinar si en algún momento se divide por 0 
        if (Double.isFinite(resultado)) {
            resultadoText = Double.toString(resultado);
        } else {
            resultadoText = "Math ERROR";
        }
        return resultadoText;
    }
    
    //Metodo que evalua la expresión
    public static double evaluarExpresion(ArrayList<String> tokens) {
        
        //Instanciar stacks para aplicar LIFO
        Stack<Double> numeros = new Stack<>();
        Stack<Character> operadores = new Stack<>();
        
        //Realizar hasta el ultimo token
        for (String token : tokens) {
            
            /*Va ir validando cada token, 
            Se encarga de hacer cada operacion logica que esten dentro de un parentesis y cuando este terminado volvera a hacer el mismo proceso 
            con el siguiente parentesis restante y operacion logica hasta que quese solo un caracter " numero" sin operar
            */
            if (esNumero(token)) {
                double numero = Double.parseDouble(token);
                numeros.push(numero);
            } else if (token.equals("(")) {
                operadores.push('(');
            } else if (token.equals(")")) {
                //Calcular hasta encontrar cierre de parentesis
                while (!operadores.isEmpty() && operadores.peek() != '(') {
                    calcular(numeros, operadores);
                }
                operadores.pop();
                
            } else if (esOperador(token)) {
                //Calcular hasta que ya no haya precedencia
                while (!operadores.isEmpty() && tienePrecedencia(token.charAt(0), operadores.peek())) {
                    calcular(numeros, operadores);
                }
                operadores.push(token.charAt(0));
            }
        }

        //Calcula todas operaciones restantes
        while (!operadores.isEmpty()) {
            calcular(numeros, operadores);
        }

        return numeros.pop();
    }

    //Metodo para calcular según operador y numero.
    public static void calcular(Stack<Double> numeros, Stack<Character> operadores) {
        double b = numeros.pop();
        double a = numeros.pop(); 
        
        char operador = operadores.pop();
        double resultado = 0;

        switch (operador) {
            case '+':
                resultado = a + b;
                break;
            case '-':
                resultado = a - b;
                break;
            case '*':
                resultado = a * b;
                break;
            case '/':
                resultado = a / b;
                break;
        }

        numeros.push(resultado);
    }

    //Metodo para determinar la precedencia "operara dependiendo las reglas de la aritmetica, sumas restas, multiplicaciones o divisiones
    public static boolean tienePrecedencia(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) {
            return false;
        }
        return true;
    }

    //Metodo para determinar si es un numero valido boleado decimal
    public static boolean esNumero(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //Metodo para determinar si es un operador valido
    public static boolean esOperador(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
    }
}
