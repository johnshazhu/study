#pragma once

#include<android/log.h>

//#define SHOW_LOG
#ifdef SHOW_LOG
#define LOG_TAG "api_encrypt"
#define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, fmt, ##args)
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, fmt, ##args)
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, fmt, ##args)
#else
#define LOGI(fmt, args...)
#define LOGD(fmt, args...)
#define LOGE(fmt, args...)
#endif