import java.util.*;
import java.util.regex.Pattern;

public class ExpressionTree {
    private static final Pattern OPERATOR = Pattern.compile("[+\\-*/%&|^e~]|\\*\\*|\\*\\*");

    private static int Precedence(String operator) {
        switch (operator) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            case "%":
            case "&":
            case "|":
            case "^":
                return 3;
            case "~":
                return 4;
            case "**":
                return 5;
            default:
                return 0;
          }
     }

    public static List<String> infixToPostfix(String expression) { //Almacena la expresion postfija 
        List<String> postfix = new ArrayList<>();
        CustomStack<String> operatorStack = new CustomStack<>(); //Pila para operadores 
        String[] tokens = expression.split("(?=[+\\-*/%&|^e~()])|(?<=[+\\-*/%&|^e~()])"); 

        for (String token : tokens) {
            if (token.isEmpty()) {
                continue; // Salta tokens vacíos
            }
            if (OPERATOR.matcher(token).matches() || token.equals("**")){
                    //Se verifica que la pila de operadores no esté vacía, sino lo hay lo que precede son números y se agregan en el token (número)else
                    //Se compara la precedencia del operador en token con la precedencia en el peek (cima) de la pila de operadores
                while (!operatorStack.isEmpty() && !token.equals("(")  && Precedence(token) <= Precedence(operatorStack.peek())) {
                    //Mediante pop se elimina el operador de la lista operator stack y se agrega a la lista posfija
                    postfix.add(operatorStack.pop());
                }
                //Aquí se agrega el operador(token) a la pila operator stack
                operatorStack.push(token);

           } else if (token.equals("(")) {
            // Si el token es un paréntesis izquierdo, lo agregamos a la pila "operatorStack".
            operatorStack.push(token);
           } else if (token.equals(")")) {
            // Si el token es un paréntesis derecho, manejamos aquí
                while (!operatorStack.isEmpty() && !operatorStack.peek().equals("(")) {
                // Mientras haya operadores en la pila y no se encuentre el paréntesis izquierdo correspondiente,
                // sacamos operadores de la pila y los agregamos a la lista "postfix".
                  postfix.add(operatorStack.pop());
                }
                if (!operatorStack.isEmpty() && operatorStack.peek().equals("(")) {
                // Eliminamos el paréntesis izquierdo de la pila, ya que se encontró su correspondiente.
                operatorStack.pop();
                } 
            } else {
                    //Si el token es un número se agrega a la lista de postija directamente
                postfix.add(token);
            }
        }

        while (!operatorStack.isEmpty()) {
             // Al final del proceso, verificamos si hay paréntesis izquierdos o derechos en la pila.
            // Si es así, lanzamos una excepción porque los paréntesis están desequilibrados.
                    //Cuando la lista no esté vacía 
                    //Se llama es la pila de operadores y se saca un operador y se pone en la lista postfija
            postfix.add(operatorStack.pop());
        }
         // Eliminar los paréntesis de la notación posfija

        return postfix;
    }

    public static double evaluatePostfix(List<String> postfix) {
        CustomStack<Double> operandStack = new CustomStack<>();

        for (String token : postfix) {
                    // Si al evaluar la operación postfija el token es un operador se toman los dos operados superiores y se resaliza la operación
                    //(Estos operandos se sacan de la pila permanentemente)
            if (OPERATOR.matcher(token).matches()) {
                double oper1 = operandStack.pop();
                double oper2 = operandStack.pop();
                double result = resolutionExpression(oper1, oper2, token);

                //se coloca el resultado de esto en la pila 
                operandStack.push(result);
            } else {
                    //Si el token es un numero se hace push en la pila
                    //Double para un resultado de mayor precisión(en caso de floats)
                operandStack.push(Double.parseDouble(token));
            }
        }
        //Con pop se saca el resultado final de la operación 
        return operandStack.pop();
    }

    private static double resolutionExpression(double num1, double num2, String operator) { //double for more precision
        switch (operator) {
            case "+":
                return num1 + num2;
            case "-":
                return num2 - num1;
            case "*":
                return num1 * num2;
            case "/":
                if (num2 == 0) {
                    throw new ArithmeticException("Inválido:División por cero");
                }
                return num1 / num2;

           case "%":
                return num2 % num1;

//Se necesita pasar lo numeros a enteros, para que los operadores lógicos trabajen de la manera debida
 //Ya que se necesita trabajar a nivel de bits 
 //Luego se pasará al resultado, osea decimales(En caso de ser necesario)
            case "&":
                return (double) ((int) num1 & (int) num2); 
            case "|":
                return (double) ((int) num1 | (int) num2);
            case "^": 
                return Math.pow(num2, num1);
//Se aplica a un solo numero pues se desea sacar el complemento a uno de este de este 
//Pendiente su correcta implementacion
            case "~":
                return  (double) (~ (int) num1);
//Pendiente su correcta implementacion
//Considerar posibles cambios
            case "**":
                return Math.pow(num2,num1);
           
            default:
                throw new IllegalArgumentException("Invalid Operator: " + operator);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingresa una expresión matemática: ");
        String infixExpression = scanner.nextLine();
        scanner.close();

        // Eliminar espacios innecesarios y asegurarse de que los operadores y operandos estén separados
        infixExpression = infixExpression.replaceAll("\\s+", "");
        infixExpression = infixExpression.replaceAll("((?<=[+\\-*/%&|^e~])|(?=[+\\-*/%&|^e~]))", " ");
    
        List<String> postfixExpression = infixToPostfix(infixExpression);
        System.out.println("Expresión postfija: " + postfixExpression);
        for (String token : postfixExpression) {
            System.out.print(token + " ");
                }
        double result = evaluatePostfix(postfixExpression);
        System.out.println("Resultado: " + result);
    }
}