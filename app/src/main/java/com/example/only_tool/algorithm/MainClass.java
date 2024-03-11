package com.example.only_tool.algorithm;

import android.content.Intent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by zhangqy
 * Data : 2024/3/9
 */
public class MainClass {
    public static void main(String[] args){
        try(Scanner scanner = new Scanner(System.in)) {
            String line = scanner.nextLine();
            solution(line);
        }
    }

    public static void solution(String line){
        printMsg(line);
        Map<Integer,Integer> map = new HashMap<>();
        Arrays.stream(line.split(" ")).forEach(x -> {
            int i = Integer.parseInt(x);
            map.put(i, map.getOrDefault(i,0) + 1);
        });

        Integer max = map.values().stream().max(Integer::compareTo).get();


    }

    public static void printMsg(String msg){
        System.out.println(msg + "\n");
    }
}
