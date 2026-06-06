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

    public static void printMsg(String msg ){
        System.out.println(msg);
    }
}
