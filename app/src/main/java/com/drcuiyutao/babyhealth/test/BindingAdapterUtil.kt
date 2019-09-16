package com.drcuiyutao.babyhealth.test

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

object BindingAdapterUtil {
    @BindingAdapter("app:imageUrl")
    @JvmStatic fun imageUrl(view: ImageView, url: String) {
        Picasso.Builder(view.context)
            .listener { picasso, uri, exception -> exception.printStackTrace()}
            .build()
            .load(url)
            .into(view, object : Callback {
                override fun onSuccess() {
                    Log.i("xdebug", "onSuccess")
                }

                override fun onError() {
                    Log.i("xdebug", "onError")
                }
            })
    }
}