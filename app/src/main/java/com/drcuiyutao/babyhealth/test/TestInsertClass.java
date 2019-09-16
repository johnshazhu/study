package com.drcuiyutao.babyhealth.test;

import android.util.Log;
import com.drcuiyutao.lib.util.TestA;
import com.lib.annotation.Insert;

public class TestInsertClass {
//    @Insert(target = TestA.class, name = "test", replace = true)
    public void test() {
        Log.i("TestInsertClass", "insert test");
    }

    public static void callInKotlin() {
        Log.i("xdebug", "callInKotlin");
    }
}
