package com.study.doc.third

object FFmpegUtil {
    external fun video_decode(input: String, output: String)
    init {
        System.loadLibrary("ffmpeg")
        System.loadLibrary("native_lib")
    }
}