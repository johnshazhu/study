package com.third.image.picasso

import android.util.Log
import android.widget.ImageView
import com.lib.annotation.Insert
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

object PicassoUtil {
    val TAG = PicassoUtil::class.java.simpleName

    @Insert(classPath = "com.drcuiyutao.lib.image.ImageUtil", name = "displayImage", replace = true)
    fun displayImage(url: String, view: ImageView) {
        Log.i("PicassoUtil", "displayImage use Picasso!!!")
        Picasso.Builder(view.context)
            .listener { picasso, uri, exception -> exception.printStackTrace()}
            .build()
            .load(url)
            .into(view, object : Callback {
                override fun onSuccess() {
                    Log.i("PicassoUtil", "onSuccess")
                }

                override fun onError() {
                    Log.i("PicassoUtil", "onError")
                }
            })
    }
}