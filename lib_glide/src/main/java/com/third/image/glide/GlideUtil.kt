package com.third.image.glide

import android.net.Uri
import android.util.Log
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.lib.annotation.Inject

object GlideUtil {
//    private val TAG = GlideUtil::class.java.simpleName

    @Inject(classPath = "com.study.lib.image.ImageUtil", name = "displayImage", replace = true)
    fun displayImage(url: String, view: ImageView) {
        Log.i("GlideUtil", "displayImage use Glide!!!")
        Glide.with(view.context)
            .asBitmap()
            .load(Uri.parse(url))
            .override(640, 360)
            .fitCenter()
            .into(view)
    }
}
