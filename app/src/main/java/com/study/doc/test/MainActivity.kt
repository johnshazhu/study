package com.study.doc.test

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.Intent.ACTION_OPEN_DOCUMENT
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View.GONE
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.study.doc.api.APIService
import com.study.doc.api.CommentApiService
import com.study.doc.model.LoginViewModel
import com.study.lib.api.base.*
import com.study.doc.databinding.ActivityMainBinding
import com.study.doc.test.widget.MyAdapter
import com.study.lib.util.ClassFileParse
import com.study.lib.util.LogUtil
import com.study.lib.util.TestA
import com.study.lib.util.TestB
import com.google.gson.Gson
import com.study.doc.R
import com.study.doc.third.FFmpegUtil
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : BaseActivity(), ResponseListener<APIBaseResponse<StartUpData>> {
    private val data = TestData()
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        data.text = "This is Hello World!"
        data.color = 0xFF4A4A or (0xFF0000 shl 8)
        data.imageUrl = "https://qns.ivybaby.me//20190705/20190705103454/ea58752472d8486695e0de182fad0d29.jpg?imageView2/1/w/640/h/360"

        val context = this
        binding.test = data
        binding.image.setOnClickListener {
            startActivity(Intent(context, TestPagerActivity::class.java))
            /*val input = Environment.getExternalStorageDirectory().absolutePath + "/testq.mp4"
            val output = Environment.getExternalStorageDirectory().absolutePath + "/video_frame.jpeg"
            Observable
                .create(ObservableOnSubscribe<String> {
                    FFmpegUtil.video_decode(input, output)
                    it.onComplete()
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<String> {
                    override fun onComplete() {
                        LogUtil.debug("onComplete")
                    }

                    override fun onSubscribe(d: Disposable) {
                        LogUtil.debug("onSubscribe")
                    }

                    override fun onNext(t: String) {
                        LogUtil.debug("onNext")
                    }

                    override fun onError(e: Throwable) {
                        LogUtil.debug("onError")
                    }
                })*/
        }
        binding.viewmodel = LoginViewModel()
        val myDataset = Array(26) { i -> (i + 65).toChar().toString() }
        viewManager = LinearLayoutManager(this)
        viewAdapter = MyAdapter(myDataset)
        binding.recyclerView.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            layoutManager = viewManager

            adapter = viewAdapter
        }
        binding.recyclerView.visibility = GONE

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
            if (targetSdkVersion >= android.os.Build.VERSION_CODES.Q && android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.Q) {
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
