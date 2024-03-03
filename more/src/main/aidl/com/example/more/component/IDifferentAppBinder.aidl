// IDifferentAppBinder.aidl
package com.example.more.component;

// Declare any non-default types here with import statements

interface IDifferentAppBinder {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    void setData(String data);
}