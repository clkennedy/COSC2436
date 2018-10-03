/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pa2;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author cameron.kennedy
 */
public class LispEvaluator {
    private static Stack<String> lEval = new Stack<>();
    private static String lExpression;
    private static String errorMessage;
    
    private LispEvaluator(){
        
    }
    
    public static boolean ValidateLispExpression(String lispExpression){
        int fParen = 0;
        int bParen = 0;
        
        boolean nextHasToBeOperand = false;
        boolean nextHasToBeDigitOrFParen = false;
        boolean openExpression = false, closeExpression = false;
        
        for(int i = 0; i < lispExpression.length(); i++){
            if(!isValidateCharacter(""+lispExpression.charAt(i))){
                lEval.push("Invalid Character at index (" + i + "): " +lispExpression.charAt(i));
                return false;
            }
            
            if(lispExpression.charAt(i) == ')'){
                bParen ++;
                if(fParen == bParen && openExpression) closeExpression = true;
                if(nextHasToBeOperand || nextHasToBeDigitOrFParen){
                    lEval.push("Invalid Character at index (" + i + "): " + lispExpression.charAt(i) + ", Has to be Operator or '('");
                    return false;
                }
            }
            if(lispExpression.charAt(i) == '('){
                fParen ++;
                if(closeExpression){
                    lEval.push("Invalid Expression, only 1 Lisp Expression at a time");
                    return false;
                }
                if(!openExpression && !closeExpression) openExpression = true;
                if(nextHasToBeOperand){
                    lEval.push("Invalid Character at index (" + i + "): " + lispExpression.charAt(i) + ", Has to be Operator");
                    return false;
                }
                nextHasToBeOperand = true;
                nextHasToBeDigitOrFParen = false;
            }
            if(isDigit(lispExpression.charAt(i) + "")){
                if(nextHasToBeOperand){
                    lEval.push("Invalid Character at index (" + i + "): " + lispExpression.charAt(i) + ", Has to be Operator");
                    return false;
                }
                 nextHasToBeDigitOrFParen = false;
            }
            if(isOperand(lispExpression.charAt(i) + "")){
                if(!nextHasToBeOperand){
                    lEval.push("Invalid Character at index (" + i + "): " + lispExpression.charAt(i) + ", Has to be Operator");
                    return false;
                }
                nextHasToBeOperand = false;
                if(lispExpression.charAt(i) == '-' || lispExpression.charAt(i) == '/'){
                    nextHasToBeDigitOrFParen = true;
                }
            }
        }
        lExpression = lispExpression;
        if(fParen != bParen)lEval.push("Uneven number of Open and Close Parenthesis");
        return fParen == bParen;
    }
    
    private static boolean isValidateCharacter(String str){
        return isDigit(str) || isOperand(str) || isParenOrSpace(str);
    }
    
    private static boolean isDigit(String str){
        try{
            Integer.parseInt(str);
        }catch(Exception e){
            return false;
        }
        return true;
    }
    private static boolean isOperand(String str){
        return str.equals("+") || str.equals("-") || str.equals("*") || str.equals("/");
    }
    private static boolean isParenOrSpace(String str){
        return str.equals(")") || str.equals("(") || str.equals(" ");
    }
    
    public static String GetResult(String str){
        lExpression = str;
        return GetResult();
    }
    
    public static String GetResult(){
        
        if(!ValidateLispExpression(lExpression)) return lEval.pop();
        
        boolean digit = false;
        int dStart = 0;
        int dEnd = 0;
        
        for(int i = 0; i < lExpression.length(); i++){
            if(isOperand(lExpression.charAt(i) + "")){
                lEval.push(lExpression.charAt(i) + "");
            }
            if(lExpression.charAt(i) == ' '){
                if(digit){
                    dEnd = i;
                    lEval.push(lExpression.substring(dStart,dEnd));
                }
                digit = false;
            }
            if(isDigit(lExpression.charAt(i) + "")){
                if(!digit) dStart = i;
                digit = true;
            }
            if(lExpression.charAt(i) == ')'){
                if(digit){
                    dEnd = i;
                    lEval.push(lExpression.substring(dStart,dEnd));
                    digit = false;
                }
                String expression = "";
                while(!isOperand(lEval.peek())){
                    expression += lEval.pop();
                    if(!isOperand(lEval.peek())){
                        expression += ",";
                    }
                }
                String op = lEval.pop();
                if(op.equals("+")){
                    lEval.push(AddExpression(expression) + "");
                }
                if(op.equals("*")){
                    lEval.push(MultiplyExpression(expression) + "");
                }
                if(op.equals("-")){
                    lEval.push(SubtractExpression(expression) + "");
                }
                if(op.equals("/")){
                    lEval.push(DivideExpression(expression) + "");
                }
            }
        }
        if(lEval.size() != 1)
            lEval.push("Something Went Wrong");
        return lEval.pop();
    }
    
    private static double AddExpression(String str){
        String[] nums = str.split(",");
        double result = 0;
        if(!str.equals("")){
            for(int i = 0; i < nums.length; i++){
                result += Double.parseDouble(nums[i]);
            }
        }
        return result;
    }
    
    private static double MultiplyExpression(String str){
        String[] nums = str.split(",");
        double result = 1;
        if(!str.equals("")){
            for(int i = 0; i < nums.length; i++){
                result *= Double.parseDouble(nums[i]);
            }
        }
        return result;
    }
    
    private static double SubtractExpression(String str){
        String[] nums = str.split(",");
        double result = Double.parseDouble(nums[nums.length - 1]);
        if(!str.equals("") && nums.length > 1){
            for(int i = nums.length - 2; i >= 0; i--){
                result -= Double.parseDouble(nums[i]);
            }
        }
        else{
            return Double.parseDouble("-" + result);
        }
        return result;
    }
    
    private static double DivideExpression(String str){
        String[] nums = str.split(",");
        double result = Double.parseDouble(nums[nums.length - 1]);
        if(!str.equals("") && nums.length > 1){
            for(int i = nums.length - 2; i >= 0; i--){
                result /= Double.parseDouble(nums[i]);
            }
        }
        else{
            return 1/result;
        }
        return result;
    }
    
}
