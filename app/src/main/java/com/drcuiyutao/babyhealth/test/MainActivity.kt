package com.drcuiyutao.babyhealth.test

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_OPEN_DOCUMENT
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.drcuiyutao.babyhealth.api.APIService
import com.drcuiyutao.babyhealth.api.CommentApiService
import com.drcuiyutao.lib.api.base.*
import com.drcuiyutao.babyhealth.test.databinding.ActivityMainBinding
import com.drcuiyutao.lib.util.ClassFileParse
import com.drcuiyutao.lib.util.LogUtil
import com.drcuiyutao.lib.util.TestA
import com.drcuiyutao.lib.util.TestB
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class MainActivity : BaseActivity(), ResponseListener<APIBaseResponse<StartUpData>> {
    private val data = TestData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        data.text = "This is Hello World!"
        data.color = 0xFF4A4A or (0xFF0000 shl 8)
        data.imageUrl = "https://qns.ivybaby.me//20190705/20190705103454/ea58752472d8486695e0de182fad0d29.jpg?imageView2/1/w/306/h/360"

        val context = this
        binding.test = data
        binding.image.setOnClickListener { startActivity(Intent(context, TestPagerActivity::class.java)) }

//        Test.testParseData(this)
//        Test.testRoom(this)
        TestAsm.test(this)
//        startup()
        TestAsm.testStaticModify()

        TestJava.insertKotlin()
        TestJava.insertKotlinReplace()

//        getMessageCount()
        var test = TestA()
        test.test()

        var testB = TestB()
        testB.testB()
        testB.test("Hello, World!")
        testB.testField()

        var packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        if (packageInfo != null) {
            var targetSdkVersion = packageInfo.applicationInfo.targetSdkVersion
            Log.i("xdebug", "targetSdkVersion : $targetSdkVersion")
            if (targetSdkVersion >= android.os.Build.VERSION_CODES.Q) {
                performFileSearch()
            } else {
                var permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                    requestPermissions(permissions, 100)
                } else {
                    ClassFileParse.parseClassFile(this, null)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                100 -> {
                    var uri = data?.data
                    if (uri.toString().endsWith("TestInsertUtil.class")) {
                        ClassFileParse.parseClassFile(this, uri)
                    }
                }
            }
        }
    }

    private fun performFileSearch() {
        var intent = Intent(ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        startActivityForResult(intent, 100)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            ClassFileParse.parseClassFile(this, null)
        }
    }

    private fun startup() {
        try {
            val inputReader = InputStreamReader(resources.assets.open("startup_form.json"))
            val bufReader = BufferedReader(inputReader)
            val result = StringBuilder()
            bufReader.lineSequence().forEach {
                result.append(it)
            }
            var body = Gson().fromJson(result.toString(), BaseBody::class.java)
            LogUtil.debug("startup body : ${body.getAppDevice()}")
            var service = NetworkBase.getService(APIService::class.java)
            Request.call(this, service!!.startup(body), this)
        } catch (r : Throwable) {
            r.printStackTrace()
        }
    }

    override fun onBizSuccess(response: APIBaseResponse<StartUpData>?) {
        super.onBizSuccess(response)
        Log.i("xdebug", "rsp msg : ${response?.msg}")
    }

    override fun onBizFailure(response: APIBaseResponse<StartUpData>?) {
        super.onBizFailure(response)
        Log.i("xdebug", "rsp code : ${response?.code}, bscode : ${response?.getBscode()}")
    }

    override fun onFailure(code: Int, result: String?) {
        super.onFailure(code, result)
        Log.i("xdebug", result)
    }

    private fun getMessageCount() {
        try {
            val inputReader = InputStreamReader(resources.assets.open("msg_count.json"))
            val bufReader = BufferedReader(inputReader)
            val result = StringBuilder()
            bufReader.lineSequence().forEach {
                result.append(it)
            }
            var body = Gson().fromJson(result.toString(), BaseBody::class.java)
            var service = NetworkBase.getService(CommentApiService::class.java)
            service!!.getNewMessageCount(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ rsp ->
                    if (rsp != null && rsp.isSuccess) {
                        Log.i("xdebug", "rsp msg : ${rsp.msg}, count : ${Gson().toJson(rsp.data)}")
                    }
                }
        } catch (r : Throwable) {
            r.printStackTrace()
        }
    }
}
