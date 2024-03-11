package com.example.only_tool.java;

/**
 * Created by zhangqy
 * Data : 2024/3/10
 */

import android.util.ArrayMap;

import java.sql.Array;
import java.util.HashMap;
import java.util.List;

/**
 * synchronized是Java中的关键字，synchronized可以保证方法或者代码块在运行时，同一时刻只有一个方法可以进入到临界区，
 * 同时它还可以保证共享变量的内存可见性，Java中每一个对象都可以作为锁，这是synchronized实现同步的基础。
 *
 * 它修饰的对象有以下几种：
 * 1. 修饰一个代码块，被修饰的代码块称为同步语句块，其作用的范围是大括号{}括起来的代码，作用的对象是调用这个代码块的对象；
 * 2. 修饰一个方法，被修饰的方法称为同步方法，其作用的范围是整个方法，作用的对象是调用这个方法的对象；
 * 3. 修饰一个静态的方法，其作用的范围是整个静态方法，作用的对象是这个类的所有对象；
 * 4. 修饰一个类，其作用的范围是synchronized后面括号括起来的部分，作用主的对象是这个类的所有对象。
 * 原文链接：https://blog.csdn.net/zhangqiluGrubby/article/details/80500505
 */
public class SynchronizedTestClass {
    public static void main(String[] args){
        try {
            //testSynchronizedCodeBlock();
            testSynchronizedMethod();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //1.测试同步代码快
    public static final byte[] lock = new byte[0];
    public static void testSynchronizedCodeBlock() throws InterruptedException {
        synchronized (lock){
            //此代码块内代码同步执行，上一行执行结束才能开始下一行
            for (int i = 0; i < 4 ; i++){
                printMsg("这是当前的 i = " + i);
                Thread.sleep(1000);
            }
            printMsg("for循环结束了");
        }
    }

    //2.测试同步方法
    public static void testSynchronizedMethod() throws InterruptedException {
        work("work1");
        work("work2");
        work("work3");
        printMsg("work结束");
        String[] ls = new String[3];
        HashMap<String,String> map = new HashMap<>();
        map.put("key-1","value-1");
        ArrayMap<String,String> map1 = new ArrayMap<>();
    }

    public static synchronized void work(String msg) throws InterruptedException {
        printMsg("这是work内容 :" + msg);
        Thread.sleep(3000);
    }

    public static void printMsg(String msg){
        System.out.println(msg + "\n");
    }
}
