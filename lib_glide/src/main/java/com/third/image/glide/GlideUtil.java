package com.third.image.glide;

import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lib.annotation.Insert;

public class GlideUtil {
    private static final String TAG = GlideUtil.class.getSimpleName();

    @Insert(classPath = "com.drcuiyutao.lib.image.ImageUtil", name = "displayImage", replace = true)
    public static void displayImage(String url, ImageView view) {
        Log.i(TAG, "displayImage use Glide!!!");
        Glide.with(view.getContext()).load(Uri.parse(url)).override(306, 600).fitCenter().into(view);
    }
}
