package com.study.lib.api.base

import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.internal.Util
import okio.BufferedSink
import okio.Okio
import okio.Source
import java.io.File
import java.io.IOException

open class ProgressBody(protected var file: File?, protected var contentType: String?, protected var listener: ProgressListener?) : RequestBody() {
    interface ProgressListener {
        fun transferred(cur: Long, total: Long)
    }

    override fun contentLength(): Long {
        return file!!.length()
    }

    override fun contentType(): MediaType? {
        return MediaType.parse(contentType!!)
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        var source: Source? = null
        try {
            source = Okio.source(file!!)
            var total: Long = 0
            var read: Long
            while (source?.read(sink.buffer(), SEGMENT_SIZE.toLong()).also { read = it!! } != -1L) {
                total += read
                sink.flush()
                if (listener != null) {
                    listener!!.transferred(total, contentLength())
                }
            }
        } finally {
            Util.closeQuietly(source)
        }
    }

    companion object {
        const val SEGMENT_SIZE = 4 * 1024 // okio.Segment.SIZE
    }
}