package com.example.more.kotlin;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangqy
 * Data : 2024/3/7
 */


public class TestJava {
    public static List<String> getStringList() {
        return new ArrayList<>();
    }

    public static void test() {
        System.out.println("+++++");
        try {

            Method method = TestJava.class.getMethod("getStringList", null);
            Type returnType = method.getGenericReturnType();
            if (returnType instanceof TestJava) {
                ParameterizedType type = (ParameterizedType) returnType;
                Type[] typeArguments = type.getActualTypeArguments();
                for (Type typeArgument : typeArguments) {
                    Class<?> typeArgClass = (Class<?>) typeArgument;
                    System.out.println("typeArgClass = " + typeArgClass);
                }
                printMsg("0000");
            }else {
                printMsg("]]]]]");
            }
            printMsg("====");
        }catch (Exception e ){
            e.printStackTrace();
        }
    }

    public static void printMsg(String msg ){
        System.out.println(msg);
    }
}
