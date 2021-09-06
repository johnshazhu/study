package com.study.lib.log

import android.content.Context
import android.os.Environment
import com.study.lib.util.SimpleUtil
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class Logcat2File private constructor() {
    private var mLogDumper: LogDumper? = null
    private var mPId = 0
    private fun setFolderPath(folderPath: String) {
        val folder = File(folderPath)
        if (!folder.exists()) {
            folder.mkdirs()
        }
        require(folder.isDirectory) { "The logcat folder path is not a directory: $folderPath" }
        PATH_LOGCAT = if (folderPath.endsWith("/")) folderPath else "$folderPath/"
    }

    fun start(context: Context) {
        var folderPath: String? = null
        folderPath = if (SimpleUtil.isExternalStorageWritable()) {
            Environment.getExternalStorageDirectory().absolutePath + File.separator + SimpleUtil.APP_ID
        } else {
            context.filesDir.absolutePath + File.separator + SimpleUtil.APP_ID
        }
        setFolderPath(folderPath)
        if (mLogDumper == null) {
            mLogDumper = LogDumper(mPId.toString(), PATH_LOGCAT)
        }
        mLogDumper!!.start()
    }

    fun stop() {
        mLogDumper?.stopLogs()
        mLogDumper = null
    }

    private inner class LogDumper(private val mPID: String, dir: String?) : Thread() {
        private var logcatProc: Process? = null
        private var mReader: BufferedReader? = null
        private var mRunning = true
        var cmds: String? = null
        private var out: FileOutputStream? = null
        fun stopLogs() {
            mRunning = false
        }

        override fun run() {
            try {
                logcatProc = Runtime.getRuntime().exec(cmds)
                mReader = BufferedReader(InputStreamReader(logcatProc?.inputStream), 1024)
                var line: String? = null
                while (mRunning && mReader!!.readLine().also { line = it } != null) {
                    if (!mRunning) {
                        break
                    }
                    if (line?.length?:0 == 0) {
                        continue
                    }
                    if (out != null && line!!.contains(mPID)) {
                        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.getDefault())
                        out!!.write("""${sdf.format(Date())}  $line
                            """.trimMargin().toByteArray())
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                logcatProc?.destroy()
                logcatProc = null

                try {
                    mReader?.close()
                    mReader = null
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                try {
                    out?.close()
                    out = null
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        init {
            try {
                val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
                out = FileOutputStream(File(dir, "logcat-" + sdf.format(Date()) + ".txt"), false)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            /**
             *
             * log level：*:v , *:d , *:w , *:e , *:f , *:s
             *
             * Show the current mPID process level of E and W log.
             *
             */
            // cmds = "logcat *:e *:w | grep \"(" + mPID + ")\"";
            // cmds = "logcat  | grep \"(" + mPID + ")\"";//show log of all
            // level
            // cmds = "logcat -s way";//Print label filtering information
            cmds = "logcat"
        }
    }

    companion object {
        private var INSTANCE: Logcat2File? = null
        private var PATH_LOGCAT: String? = null
        @JvmStatic
        val instance: Logcat2File?
            get() {
                if (INSTANCE == null) {
                    INSTANCE = Logcat2File()
                }
                return INSTANCE
            }
    }

    init {
        mPId = android.os.Process.myPid()
    }
}