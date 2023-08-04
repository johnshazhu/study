package com.study.doc.test;

import android.util.Log;
import com.study.lib.util.TestA;
import com.lib.annotation.Inject;

public class TestInsertClass {
    @Inject(target = TestA.class, name = "test", replace = true)
    public void test() {
        Log.i("TestInsertClass", "insert test");
    }

    public static void callInKotlin() {
        Log.i("xdebug", "callInKotlin");
    }
}
