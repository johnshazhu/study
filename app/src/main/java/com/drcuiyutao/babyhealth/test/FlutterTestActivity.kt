package com.drcuiyutao.babyhealth.test

import android.os.Bundle
import io.flutter.facade.Flutter

class FlutterTestActivity: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flutter)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.flutter_view, Flutter.createFragment("test"))
        fragmentTransaction.commit()
    }
}