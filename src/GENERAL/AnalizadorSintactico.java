package GENERAL;

import java.util.ArrayList;

public class AnalizadorSintactico {

    //Toma las listas array creadas en el analizador lexico 
    public String generarSintaxis(ArrayList<String> tokensCaracteres, ArrayList<String> tokensNumeros, ArrayList<String> tokensOperadores) {
        boolean sintaxError = false;
        String resultado = "";

        //Validar que la expresión contenga numeros, y que sean de formato correcto, en caso de estar vacias las listas o tenga un caracter erroneo dara un sintaxError
        if (tokensNumeros == null || tokensNumeros.isEmpty()) {
            sintaxError = true;
        } else {
            for (String token : tokensNumeros) {
                try {
                    Double.parseDouble(token);
                } catch (NumberFormatException e) {
                    sintaxError = true;
                    break;
                }
            }
        }
        
        //Si resulta dar sintaxError mostrara el mensaje sintax 
        if (sintaxError) {
            resultado = "Sintax ERROR";
        } else {

            //en caso de no serlo validara caracteres permitidos
            for (String token : tokensCaracteres) {
                if (!isValidCharacter(token)) {
                    sintaxError = true;
                    break;
                }
            }

            //Verificara que cada parentesis abierto este correctamente cerrado
            int openParenthesisCount = 0;
            int startIndex = -1;

            for (int i = 0; i < tokensCaracteres.size(); i++) {
                String token = tokensCaracteres.get(i);

                if (token.equals("(")) {
                    openParenthesisCount++;
                    if (openParenthesisCount == 1) {
                        startIndex = i + 1;
                    }
                } else if (token.equals(")")) {
                    openParenthesisCount--;
                    if (openParenthesisCount == 0 && startIndex != -1) {
                        // Verificar si se encontró el cierre del paréntesis correspondiente
                        boolean foundClosingParenthesis = false;
                        for (int j = startIndex; j < i; j++) {
                            if (tokensCaracteres.get(j).equals(")")) {
                                foundClosingParenthesis = true;
                                break;
                            }
                        }
                        if (!foundClosingParenthesis && openParenthesisCount != 0) {
                            sintaxError = true;
                            break;
                        }
                        startIndex = -1;
                    }
                }
            }
            if (openParenthesisCount != 0) {
                sintaxError = true;
            }

            //Validar que no hayan parentesis vacios 
            for (int i = 0; i < tokensCaracteres.size() - 1; i++) {
                String tokenActual = tokensCaracteres.get(i);
                String siguienteToken = tokensCaracteres.get(i + 1);
                if (tokenActual.equals("(") && siguienteToken.equals(")")) {
                    sintaxError = true;
                    break;
                }
            }

            //Validar que el siguiente signo despues de "+" o "-" no sea "*", "/"
            for (int i = 0; i < tokensCaracteres.size() - 1; i++) {
                String tokenActual = tokensCaracteres.get(i);
                String siguienteToken = tokensCaracteres.get(i + 1);
                if ((tokenActual.equals("+") || tokenActual.equals("-"))
                        && (siguienteToken.equals("*") || siguienteToken.equals("/") || siguienteToken.equals(")"))) {
                    sintaxError = true;
                    break;
                }
            }

            //Validar que el siguiente signo despues de "*" o "/" no sea "*", "/" o ")"
            for (int i = 0; i < tokensCaracteres.size() - 1; i++) {
                String tokenActual = tokensCaracteres.get(i);
                String siguienteToken = tokensCaracteres.get(i + 1);

                if ((tokenActual.equals("*") || tokenActual.equals("/"))
                        && (siguienteToken.equals("*") || siguienteToken.equals("/") || siguienteToken.equals(")"))) {
                    sintaxError = true;
                    break;
                }
            }

            //Valida que el ultimo caracter no sea "+", "-", "*" o "/"
            String ultimoCaracter = tokensCaracteres.get(tokensCaracteres.size() - 1);
            if (ultimoCaracter.equals("+") || ultimoCaracter.equals("-")
                    || ultimoCaracter.equals("*") || ultimoCaracter.equals("/")) {
                sintaxError = true;
            }

            //Valida que el caracter despues de ")" no sea numerico o "." ya que en calculadoras no es posible
            for (int i = 0; i < tokensCaracteres.size() - 1; i++) {
                String tokenActual = tokensCaracteres.get(i);
                String siguienteToken = tokensCaracteres.get(i + 1);

                if (tokenActual.equals(")") && (Character.isDigit(siguienteToken.charAt(0)) || siguienteToken.equals("."))) {
                    sintaxError = true;
                    break;
                }
            }
            
            //Validar que el primer caracter no sea "*" o un "/"
            if (!tokensCaracteres.isEmpty()) {
                String primerToken = tokensCaracteres.get(0);
                if (primerToken.equals("*") || primerToken.equals("/")) {
                    sintaxError = true;
                }
            }

            //Validar en primera instancia si hay alguna división entre 0
            for (int i = 0; i < tokensOperadores.size() - 1; i++) {
                String operador = tokensOperadores.get(i);
                if (operador.equals("/") && i < tokensOperadores.size() - 1) {
                    String siguienteToken = tokensOperadores.get(i + 1);
                    try {
                        double numero = Double.parseDouble(siguienteToken);
                        if (numero == 0.0) {
                            sintaxError = true;
                            break;
                        }
                    } catch (NumberFormatException e) {
                        // Ignorar si el siguiente token no es un número
                    }
                }
            }

            //Agregar "*" cuando encuentre un "(" y el anterior es un numero. 
            for (int i = 0; i < tokensOperadores.size(); i++) {
                String operador = tokensOperadores.get(i);
                if (operador.equals("(") && i > 0) {
                    String anteriorToken = tokensOperadores.get(i - 1);
                    try {
                        Double.parseDouble(anteriorToken);
                        tokensOperadores.add(i, "*");
                    } catch (NumberFormatException e) {
                        // Ignorar si el token anterior no es un número
                    }
                }
            }

            //Aplicar ley de signos 
            for (int i = 0; i < tokensOperadores.size() - 1; i++) {
                String operadorActual = tokensOperadores.get(i);
                String operadorSiguiente = tokensOperadores.get(i + 1);
                if ((operadorActual.equals("+") || operadorActual.equals("-")) && (operadorSiguiente.equals("+") || operadorSiguiente.equals("-"))) {
                    boolean resultadoPositivo = (operadorActual.equals(operadorSiguiente));
                    String nuevoOperador = resultadoPositivo ? "+" : "-";
                    tokensOperadores.set(i, nuevoOperador);
                    tokensOperadores.remove(i + 1);
                    i--; // Retroceder el índice para verificar nuevamente el operador actual con el siguiente
                }
            }

            //Para TESTEAR
            if (sintaxError) {
                resultado = "Sintax ERROR";
            } else {
                AnalizadorSemantico AnalizadorSemantico = new AnalizadorSemantico();
                resultado = AnalizadorSemantico.recibirTokens(tokensOperadores);
            }
        }
        return resultado;
    }

    //Metodo para validar caracteres permitidos
    private boolean isValidCharacter(String token) {
        String[] caracteresPermitidos = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ".", "(", ")", "+", "-", "*", "/"};
        for (String caracter : caracteresPermitidos) {
            if (caracter.equals(token)) {
                return true;
            }
        }
        return false;
    }
}
