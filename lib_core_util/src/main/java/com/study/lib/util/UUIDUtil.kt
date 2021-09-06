package com.study.lib.util

import android.os.Environment
import android.text.TextUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

object UUIDUtil {
    private const val UUID_STR = "uuid"
    private const val UUID_FILE_NAME = ".uuid"

    @JvmStatic fun getUUID(): String {
        val uuid: String = SimpleSharedPreferencesUtil.getStringConfig(UUID_STR)
        return if (TextUtils.isEmpty(uuid)) getSavedUUID() else uuid
    }

    @JvmStatic fun saveUUID(uuid: String) {
        SimpleSharedPreferencesUtil.saveStringConfig(UUID_STR, uuid)
        saveUUIDToStorage(uuid)
    }

    @JvmStatic fun saveUUIDToStorage(uuid: String) {
        if (TextUtils.isEmpty(uuid)) {
            return
        }
        if (SimpleUtil.isExternalStorageWritable()) {
            val sdcard = Environment.getExternalStorageDirectory()
            if (sdcard != null) {
                val pathBuilder = StringBuilder()
                        .append(sdcard.absolutePath)
                        .append(File.separator)
                        .append(UUID_FILE_NAME)
                val file = File(pathBuilder.toString())
                if (!file.exists()) {
                    var fos: FileOutputStream? = null
                    try {
                        if (file.createNewFile()) {
                            fos = FileOutputStream(file)
                            fos.write(uuid.toByteArray())
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        if (fos != null) {
                            try {
                                fos.close()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }
        }
    }

    @JvmStatic fun getSavedUUIDFromStorage(): String? {
        if (SimpleUtil.isExternalStorageReadable()) {
            val sdcard = Environment.getExternalStorageDirectory()
            if (sdcard != null) {
                val pathBuilder = StringBuilder()
                        .append(sdcard.absolutePath)
                        .append(File.separator)
                        .append(UUID_FILE_NAME)
                val file = File(pathBuilder.toString())
                if (file.exists()) {
                    var fis: FileInputStream? = null
                    try {
                        fis = FileInputStream(file)
                        val buf = ByteArray(256)
                        val sb = StringBuilder()
                        var len = 0
                        while (fis.read(buf).also { len = it } != -1) {
                            sb.append(String(buf, 0, len))
                        }
                        return sb.toString()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        if (fis != null) {
                            try {
                                fis.close()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }
        }
        return null
    }

    private fun getSavedUUID(): String {
        var savedUUID = getSavedUUIDFromStorage()

        //文件不存在或者文件读取失败，生成uuid
        if (TextUtils.isEmpty(savedUUID)) {
            savedUUID = UUID.randomUUID().toString()
            saveUUID(savedUUID)
        } else {
            SimpleSharedPreferencesUtil.saveStringConfig(UUID_STR, savedUUID!!)
        }
        return savedUUID
    }
}