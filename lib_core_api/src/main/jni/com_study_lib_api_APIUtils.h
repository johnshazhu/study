#include <jni.h>

#ifndef _Included_com_study_lib_api_APIUtils
#define _Included_com_study_lib_api_APIUtils
#ifdef __cplusplus
extern "C" {
#endif
JNIEXPORT jstring JNICALL Java_com_study_lib_api_APIUtils_nativeGetParam(JNIEnv *, jobject, jobject, jobject, jobject);
JNIEXPORT jstring JNICALL Java_com_study_lib_api_APIUtils_nativeGetImPassword(JNIEnv *, jobject, jobject, jobject);

#ifdef __cplusplus
}
#endif
#endif
