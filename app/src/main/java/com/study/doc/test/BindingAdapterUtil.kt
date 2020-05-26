package com.study.doc.test

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.study.lib.image.ImageUtil

object BindingAdapterUtil {
    @BindingAdapter("app:imageUrl")
    @JvmStatic fun imageUrl(view: ImageView, url: String) {
        ImageUtil.displayImage(url, view)
    }
}