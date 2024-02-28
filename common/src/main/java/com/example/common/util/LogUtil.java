package com.example.common.util;

import android.util.Log;

public class LogUtil {

    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int NOTHING = 6;
    public static final int PRINT_ALL = 0;

    /**控制想要打印的日志级别
     * 等于NOTHING，则会屏蔽掉所有日志*/
    public static final int LEVEL = PRINT_ALL;

    public static void v(String tag, String msg){
        if(LEVEL == PRINT_ALL){
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg){
        if(LEVEL == PRINT_ALL){
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg){
        if(LEVEL == PRINT_ALL){
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg){
        if(LEVEL == PRINT_ALL){
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg){
        if(LEVEL == PRINT_ALL){
            Log.e(tag, msg);
        }
    }

}