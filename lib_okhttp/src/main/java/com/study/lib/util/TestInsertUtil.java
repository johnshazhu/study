package com.study.lib.util;

import android.util.Log;

import com.lib.annotation.Inject;

public class TestInsertUtil {
//    private static final String TAG = TestInsertUtil.class.getSimpleName();

//    @Inject(classPath = "com.study.doc.test.TestJava$Companion", name = "TAG")
    @Inject(classPath = "com.study.lib.util.TestB", name = "TAG")
    private static final String TAG = "TestJava";

    @Inject(classPath = "com.study.lib.util.TestB", name = "COUNT")
    private static final int COUNT = 5;

    @Inject(classPath = "com.study.lib.util.TestB", name = "testB", replace = true)
    public void testB() {
        Log.i("TestInsertUtil", "testB third inject core");
    }

    @Inject(classPath = "com.study.lib.util.TestB", name = "test", replace = true)
    public void test(String msg) {
        System.out.println(msg);
        Log.i("TestInsertUtil", "inject core-insert after");
    }

    @Inject(classPath = "com.study.doc.test.TestJava$Companion", name = "insertKotlin")
    public void testKotlin() {
        Log.i("TestInsertUtil", "inject to kotlin companion ---> after");
    }

    @Inject(classPath = "com.study.doc.test.TestJava$Companion", name = "insertKotlinReplace", replace = true)
    public static void testKotlinReplace() {
        Log.i("TestInsertUtil", "inject to kotlin companion ---> replace");
    }

    @Inject(classPath = "com.study.lib.util.TestB", name = "testCode", before = true)
    public static boolean testCode(String msg) {
        System.out.println(msg);
        Log.i("TestInsertUtil", "inject before testCode");
        return true;
    }
}
