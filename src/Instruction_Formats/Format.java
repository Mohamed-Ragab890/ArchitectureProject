package Instruction_Formats;

import Operations.InfixToPostfix;

import java.util.HashMap;
import java.util.Map;

public class Format {

    private static int registerCounter = 1;
    private static final Map<String, String> registerMap = new HashMap<>(); // Map to store the register for each operand

    public static String generateInstructions(String expression, int format) {
        // Reset register counter for each expression
        registerCounter = 1;
        System.out.println("Expression: " + expression);
        String postfix = InfixToPostfix.convertToPostfix(expression);
        String[] postfixTokens = postfix.split(" ");
        String[] tokens = expression.split(" ");
        StringBuilder result = new StringBuilder();

        switch (format) {
            case 4 -> result.append(generateThreeAddress(tokens));
            case 3 -> result.append(generateTwoAddress(tokens));
            case 2 -> result.append(generateOneAddress(tokens));
            case 1 -> result.append(generateZeroAddress(postfixTokens));
            default -> throw new IllegalArgumentException("Invalid instruction format");
        }

        return result.toString();
    }

    private static String getOrCreateRegister(String operand1, String operand2) {
        String resultRegister;
        if (registerMap.containsKey(operand1) && registerMap.containsKey(operand2)) {
            // If both operands are already in the register map, use the same register
            resultRegister = registerMap.get(operand1);
        } else {
            // If one of the operands is not in the register map, create a new register
            resultRegister = "R" + registerCounter++;
        }
        // Add the result register to the register map
        registerMap.put(operand1, resultRegister);
        registerMap.put(operand2, resultRegister);
        return resultRegister;
    }

    private static String generateThreeAddress(String[] tokens) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < tokens.length; i += 2) {
            if (i + 2 < tokens.length) {
                String operand1 = tokens[i];
                String operator = tokens[i + 1];
                String operand2 = tokens[i + 2];

                String resultRegister = getOrCreateRegister(operand1, operand2);

                // Check if the operand is a constant or a register
                String op1 = Character.isDigit(operand1.charAt(0)) ? operand1 : "R" + operand1;
                String op2 = Character.isDigit(operand2.charAt(0)) ? operand2 : "R" + operand2;

                result.append(getOperationCode(operator)).append(" ").append(resultRegister)
                        .append(", ").append(op1).append(", ").append(op2).append("\n");
            }
        }

        // Reset register map for the next expression
        registerMap.clear();

        return result.toString();
    }




    private static String generateTwoAddress(String[] tokens) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < tokens.length - 1; i += 2) {
            String operand = tokens[i];
            String operator = tokens[i + 1];
            result.append(getOperationCode(operator)).append(" R1, ").append(operand).append("\n");
        }
        return result.toString();
    }

    private static String generateOneAddress(String[] tokens) {
        StringBuilder result = new StringBuilder();
        for (String token : tokens) {
            if (isOperator(token)) {
                result.append(getOperationCode(token)).append("\n");
            } else {
                result.append("LOAD ").append(token).append("\n");
            }
        }
        return result.toString();
    }

    private static String generateZeroAddress(String[] tokens) {
        StringBuilder result = new StringBuilder();
        for (String token : tokens) {
            if (isOperator(token)) {
                result.append(getOperationCode(token)).append("\n");
            } else {
                result.append("PUSH ").append(token).append("\n");
            }
        }
        return result.toString();
    }


    private static String getOperationCode(String operator) {
        return switch (operator) {
            case "+" -> "ADD";
            case "-" -> "SUBT";
            case "*" -> "MULT";
            case "/" -> "DIV";
            case "%" -> "MOD";
            case "=" -> "STORE";
            default -> throw new IllegalArgumentException("Invalid operator: " + operator);
        };
    }

    private static boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/") || token.equals("%");
    }
}
