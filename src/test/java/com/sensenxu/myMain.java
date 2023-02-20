package com.sensenxu;

import java.util.Stack;

public class myMain {
    public static void main(String[] args) {
        System.out.println(isValid("{}[]"));
    }
    public static boolean isValid(String s) {
        Stack stack = new Stack<Character>();
        for(int i = 0; i < s.length(); i++){
            if(s.charAt(i) == '(' || s.charAt(i) == '[' || s.charAt(i) == '{')
                stack.push(s.charAt(i));
            else{
                Character c = (Character) stack.pop();
                if(s.charAt(i) == ')')if(c != '(')return false;
                if(s.charAt(i) == ']')if(c != '[')return false;
                if(s.charAt(i) == '}')if(c != '{')return false;
            }
        }
        return true;

    }
}
