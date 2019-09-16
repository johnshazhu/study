package com.drcuiyutao.lib.util;

import android.util.Log;

import com.lib.annotation.Insert;

public class TestInsertUtil {
//    private static final String TAG = TestInsertUtil.class.getSimpleName();

//    @Insert(classPath = "com.drcuiyutao.babyhealth.test.TestJava$Companion", name = "TAG")
    @Insert(classPath = "com.drcuiyutao.lib.util.TestB", name = "TAG")
    private static final String TAG = "TestJava";

    @Insert(classPath = "com.drcuiyutao.lib.util.TestB", name = "COUNT")
    private static final int COUNT = 5;

//    @Insert(classPath = "com.drcuiyutao.lib.util.TestB", name = "testB", replace = true)
    public void testB() {
        Log.i("TestInsertUtil", "testB third inject core");
    }

//    @Insert(classPath = "com.drcuiyutao.lib.util.TestB", name = "test")
    public void test(String msg) {
        System.out.println(msg);
        Log.i("TestInsertUtil", "inject core-insert after");
    }

    @Insert(classPath = "com.drcuiyutao.babyhealth.test.TestJava$Companion", name = "insertKotlin")
    public void testKotlin() {
        Log.i("TestInsertUtil", "inject to kotlin companion ---> after");
    }

    @Insert(classPath = "com.drcuiyutao.babyhealth.test.TestJava$Companion", name = "insertKotlinReplace", replace = true)
    public static void testKotlinReplace() {
        Log.i("TestInsertUtil", "inject to kotlin companion ---> replace");
    }
}
