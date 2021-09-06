package com.study.lib.util;

import android.util.Log;

public class TestB {
    private static final String TAG = null;
    private static final String TAG2 = null;
    private static final int COUNT = "replace".length();

    public void testB() {

    }

    public void test(String msg) {
        Log.i("xdebug", "test insert after");
    }

    public void testField() {
        Log.i("xdebug",  "modify field " + (TAG == null ? "fail" : "success"));
        showCount(COUNT);
    }

    public void showCount(int count) {
        Log.i("xdebug", "count : " + count);
    }

    public static void testCode(String msg) {
        Log.i("xdebug", "testCode original code");
    }
}
