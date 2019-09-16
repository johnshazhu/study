package com.drcuiyutao.babyhealth.test;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class TestAsm {
    private static String TAG = null;
    public static void test(Context context) {
//        showLog();
        Toast.makeText(context, "This is a test", Toast.LENGTH_LONG).show();
    }

    public static void showLog() {
        Log.i("xdebug", "TestAsm call showLog method. TAG : " + TAG);
    }

    public static void testStaticModify() {
        Log.i("xdebug", "testStaticModify tag : " + TAG);
    }
}
