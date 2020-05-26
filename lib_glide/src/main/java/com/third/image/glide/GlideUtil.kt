package com.third.image.glide

import android.net.Uri
import android.util.Log
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.lib.annotation.Insert

object GlideUtil {
    private val TAG = GlideUtil::class.java.simpleName

    @Insert(classPath = "com.com.study.lib.image.ImageUtil", name = "displayImage", replace = true)
    fun displayImage(url: String, view: ImageView) {
        Log.i("GlideUtil", "displayImage use Glide!!!")
        Glide.with(view.context).load(Uri.parse(url)).override(306, 600).fitCenter().into(view)
    }
}
