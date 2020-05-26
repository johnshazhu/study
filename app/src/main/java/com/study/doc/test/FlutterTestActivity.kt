package com.study.doc.test

import android.os.Bundle
import android.view.View
import io.flutter.facade.Flutter

class FlutterTestActivity: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flutter)
    }

    fun onRandomWordsClick(v: View) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.flutter_view, Flutter.createFragment("index"))
        fragmentTransaction.commit()
    }

    fun onApiTestClick(v: View) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.flutter_view, Flutter.createFragment("test"))
        fragmentTransaction.commit()
    }
}