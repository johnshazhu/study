package com.third.image.glide

import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.request.target.ImageViewTarget

class ImageTransformUtil(private val target: ImageView): ImageViewTarget<Bitmap>(target) {
    override fun setResource(resource: Bitmap?) {
        view.setImageBitmap(resource)

        resource?.let {
            val scale = target.width * 1f / resource.width
            target.layoutParams.width = target.width
            target.layoutParams.height = (resource.height * scale).toInt()
        }
    }
}