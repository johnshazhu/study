package com.drcuiyutao.babyhealth.test

import android.content.Context
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.drcuiyutao.lib.api.base.APIBaseResponse
import com.drcuiyutao.lib.db.entity.User
import com.drcuiyutao.lib.db.util.DatabaseUtil
import com.drcuiyutao.lib.gson.TypeUtil
import com.google.gson.Gson
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.*

object Test {
    fun test(context: Context, url: String, view: ImageView) {
        Picasso.Builder(context).listener { picasso, uri, exception -> exception.printStackTrace() }.build().load(url)
            .into(view, object : Callback {
                override fun onSuccess() {

                }

                override fun onError() {

                }
            })
    }

    fun type(raw: Class<*>, vararg args: Type): ParameterizedType {
        return object : ParameterizedType {
            override fun getRawType(): Type {
                return raw
            }

            override fun getActualTypeArguments(): Array<Type> {
                return arrayOf(*args)
            }

            override fun getOwnerType(): Type? {
                return null
            }
        }
    }

    fun testParseData(context: Context) {
        try {
            val inputReader = InputStreamReader(context.resources.assets.open("startup.json"))
            val bufReader = BufferedReader(inputReader)
            val result = StringBuilder()
            bufReader.lineSequence().forEach {
                result.append(it)
            }

            var type: Type = TypeUtil.type(APIBaseResponse::class.java, (StartTest::class.java.genericSuperclass as ParameterizedType).actualTypeArguments[0])
            var gson = Gson()
            var rsp: APIBaseResponse<StartUpData> = gson.fromJson(result.toString(), type)
            Log.i("xdebug", "success : " + rsp.isSuccess + ", bscode : " + rsp.getBscode())
            if (rsp.isSuccess) {
                Toast.makeText(context, "成功", Toast.LENGTH_SHORT).show()
                var data = rsp.data
                data?.appConfigSwitchList?.forEach {
                    Log.i("xdebug", "type : " + it.type + ", status : " + it.status)
                }

                data?.list?.forEach {
                    Log.i("xdebug", "skip model type : " + it.type + ", url : " + it.tourl)
                }

                Log.i("xdebug", "mallTab title : " + data?.mallTab?.title + ", icon : " + data?.mallTab?.selectedIco)

                Log.i("xdebug", "vipTab title : " + data?.vipTab?.title + ", icon : " + data?.vipTab?.selectedIco)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun testRoom(context: Context) {
        val db = DatabaseUtil.getDatabase(context)
        val user = User(UUID.randomUUID().toString(), "breeze", "@163.com", 35)
        val user2 = User(UUID.randomUUID().toString(), "john", "wick", 18)
        val dao = db?.userDao()

        if (dao != null) {
            dao.insertAll(user, user2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : CompletableObserver {
                    override fun onComplete() {
                        Log.i("xdebug", "onComplete")
                    }

                    override fun onSubscribe(d: Disposable) {
                        Log.i("xdebug", "onSubscribe")
                    }

                    override fun onError(e: Throwable) {
                        Log.i("xdebug", "onError")
                        e.printStackTrace()
                    }
                })

            dao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { entities ->
                    entities!!.forEach {
                        Log.i("xdebug", "id : " + it.uid + ", firstName : " + it.firstName)
                    }
                }
        }
    }
}
