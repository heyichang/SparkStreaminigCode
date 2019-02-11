package com.ceiec.graph.test.testForField;

import java.lang.reflect.Field;

/**
 * @author:heyichang
 * @description:
 * @date:Created in 14:16 2018-03-07
 */
public class testForget {
    public static void main(String[] args) {
        Hello hello = new Hello("heyichang","other char");
        Class clazz = hello.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field:fields
             ) {
            field.setAccessible(true);
            try {
                System.out.println(field.get(hello));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
    static class Hello{
        public String name;
        public String otherChar;

        public Hello(String name, String otherChar) {
            this.name = name;
            this.otherChar = otherChar;
        }
    }
}
