package Instruction_Formats;

import java.util.Random;

public class Expression {
    public static String expression() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            sb.append("(");
            sb.append(term());
            sb.append(")");
            sb.append(operator());
        }
        sb.delete(sb.length()-1,sb.length());

        return sb.toString();
    }
    private static String term() {

        return number() + operator() + number();
    }

    private static String operator(){
        Random rand = new Random();
        int n = rand.nextInt(4);

        return switch (n){
            case 0 -> "+";
            case 1 -> "-";
            case 2 -> "*";
            case 3 -> "/";
            default -> "";
        };
    }
    private static int number(){
        Random rand = new Random();
        return rand.nextInt(1,100);
    }
}
