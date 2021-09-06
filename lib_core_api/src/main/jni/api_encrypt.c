#include "com_study_lib_api_APIUtils.h"
#include "log.h"
#include <string.h>
#include <stdio.h>

const char *global_app_packageName = "com.study.doc";
const char *global_comparator_class = "com/study/lib/api/ComparatorParam";
const char *global_util_class = "com/study/lib/util/Util";
const int global_app_signature_hash_code_debug = 393191301;
const int global_app_signature_hash_code = 1128706192;
const char *global_ps = "nauyeuxvy";
const char *global_pw_key = "!@#$#@#@$#*&^&*&";
const char *global_invalid_signature = "invalid_signature";

#define SIGNATURE_NONE (0)
#define SIGNATURE_OK (1)
#define SIGNATURE_ILLEGAL (2)
int mSignatureStatus = SIGNATURE_NONE;

jvalue JNU_CallMethodByName(JNIEnv *env, jboolean *hasException, jobject obj, const char *name, const char *descriptor, ...);
jstring getParam(JNIEnv * env, jobject thiz, jobject context, jobject headers, jobject params);
jstring getImPassword(JNIEnv * env, jobject thiz, jobject context, jobject key);
void checkPackage(JNIEnv * env, jobject thiz, jobject context);
char* getString(JNIEnv * env, jobject thiz, jobject context, jobject params);

JNIEXPORT jstring JNICALL Java_com_study_lib_api_APIUtils_nativeGetParam(JNIEnv * env, jobject thiz, jobject context, jobject headers, jobject params) {
    return getParam(env, thiz, context, headers, params);
}

JNIEXPORT jstring JNICALL Java_com_study_lib_api_APIUtils_nativeGetImPassword(JNIEnv * env, jobject thiz, jobject context, jobject key) {
    return getImPassword(env, thiz, context, key);
}

jstring getImPassword(JNIEnv * env, jobject thiz, jobject context, jobject key) {
    LOGI("getImPassword");
    checkPackage(env, thiz, context);

    LOGI("getImPassword mSignatureStatus[%d]" , mSignatureStatus);
    if (SIGNATURE_ILLEGAL != mSignatureStatus) {
        if (NULL != key) {
            jbyte *cKey = (*env)->GetStringUTFChars(env, key, NULL);
            if (NULL != cKey) {
                int size = strlen(cKey) + strlen(global_pw_key) + 1;
                char *temp = malloc(size + 1);
                LOGI("getImPassword temp[%p]", temp);
                if (NULL != temp) {
                    strcpy(temp, cKey);
                    strcat(temp, global_pw_key);
                }
                jclass clazz_util = (*env)->FindClass(env, global_util_class);
                LOGI("getImPassword clazz_util[%p]", clazz_util);
                if (!((*env)->ExceptionCheck(env) || NULL == clazz_util)) {
                    jmethodID mid_md5 = (*env)->GetStaticMethodID(env, clazz_util, "getMD5Str", "(Ljava/lang/String;)Ljava/lang/String;");
                    LOGI("getImPassword mid_md5[%p]", mid_md5);
                    if (!((*env)->ExceptionCheck(env) || NULL == mid_md5)) {
                        LOGI("getImPassword temp[%s]", temp);
                        jstring result = (jstring)(*env)->CallStaticObjectMethod(env, clazz_util, mid_md5, (*env)->NewStringUTF(env, temp));
                        if (NULL != temp) {
                            free(temp);
                            temp = NULL;
                        }
                        (*env)->ExceptionClear(env);
                        return result;
                    }
                }
                if (NULL != temp) {
                    free(temp);
                    temp = NULL;
                }
            }
        }
    } else {
        // 非法包
    }
    LOGI("getImPassword ExceptionClear");
    (*env)->ExceptionClear(env);
    LOGI("getImPassword return");
    return NULL;
}

int comparePackageName(const char* packageName) {
    char* packages[] = {
            global_app_packageName,
            global_ggj_packageName,
    };
    int size = sizeof(packages) / sizeof(char*);
    LOGI("nativeGetParam comparePackageName packages size %d", size);
    for (int i = 0; i < size; ++i) {
        if (0 == strncmp(packageName, packages[i], strlen(packageName))) {
            return 0;
        }
    }

    return 1;
}

void checkPackage(JNIEnv * env, jobject thiz, jobject context) {
    if (SIGNATURE_OK != mSignatureStatus) {
        jboolean hasException;
        //获取包名
        jstring jstr_packageName = (jstring) JNU_CallMethodByName(env, &hasException, context, "getPackageName", "()Ljava/lang/String;").l;
        const char* loc_str_app_packageName = NULL;
        LOGI("nativeGetParam jstr_packageName[%p]" , jstr_packageName);
        if (!((*env)->ExceptionCheck(env) || jstr_packageName == NULL)) {
            //获取包名的字符串
            loc_str_app_packageName = (*env)->GetStringUTFChars(env, jstr_packageName, NULL);
            LOGI("nativeGetParam loc_str_app_packageName[%s]" , loc_str_app_packageName);
            if (NULL != loc_str_app_packageName) {
                //当前应用包名与合法包名对比
                LOGI("nativeGetParam global_app_packageName[%s]" , global_app_packageName);
                if (0 == comparePackageName(loc_str_app_packageName)) {
                    // 获得应用包的管理器
                    jobject package_manager = JNU_CallMethodByName(env, &hasException, context, "getPackageManager", "()Landroid/content/pm/PackageManager;").l;
                    LOGI("nativeGetParam package_manager[%p]" , package_manager);
                    if (!((*env)->ExceptionCheck(env) || package_manager == NULL)) {
                        // 获得应用包的信息
                        jobject package_info = JNU_CallMethodByName(env, &hasException, package_manager, "getPackageInfo",
                                                                    "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;",
                                                                    jstr_packageName, 64).l;
                        LOGI("nativeGetParam package_info[%p]" , package_info);
                        if (!((*env)->ExceptionCheck(env) || package_info == NULL)) {
                            // 获得 PackageInfo 类
                            jclass pi_clazz = (*env)->GetObjectClass(env, package_info);

                            // 获得签名数组属性的 ID
                            jfieldID fieldID_signatures = (*env)->GetFieldID(env, pi_clazz, "signatures", "[Landroid/content/pm/Signature;");
                            (*env)->DeleteLocalRef(env, pi_clazz);
                            // 得到签名数组
                            jobjectArray signatures = (*env)->GetObjectField(env, package_info, fieldID_signatures);
                            LOGI("nativeGetParam signatures[%p]" , signatures);
                            if (!((*env)->ExceptionCheck(env) || signatures == NULL)) {
                                // 得到签名
                                jobject signature = (*env)->GetObjectArrayElement(env, signatures, 0);
                                LOGI("nativeGetParam signature[%p]" , signature);
                                if (!((*env)->ExceptionCheck(env) || signature == NULL)) {
                                    //获取当前应用hashcode
                                    int hash_code = JNU_CallMethodByName(env, &hasException, signature, "hashCode", "()I").i;
                                    LOGI("nativeGetParam hash_code[%d]" , hash_code);
                                    if (!(*env)->ExceptionCheck(env)) {
                                        if ((hash_code == global_app_signature_hash_code)
                                            || (hash_code == global_app_signature_hash_code_ggj)
                                            || (hash_code == global_app_signature_hash_code_debug)) {
                                            mSignatureStatus = SIGNATURE_OK;
                                        } else {
                                            mSignatureStatus = SIGNATURE_ILLEGAL;
                                        }
                                    } else {
                                        LOGI("nativeGetParam can't get hash_code of signature");
                                    }
                                } else {
                                    LOGI("nativeGetParam can't get obj of signature");
                                }
                            } else {
                                LOGI("nativeGetParam can't get jobjectArray of signatures");
                            }
                        } else {
                            LOGI("nativeGetParam can't get obj of package_info");
                        }
                    } else {
                        LOGI("nativeGetParam can't get obj of getPackageManager");
                    }
                } else {
                    LOGI("nativeGetParam this app is illegal");
                    mSignatureStatus = SIGNATURE_ILLEGAL;
                }
            } else {
                LOGI("nativeGetParam can't get packagename from jstring");
            }
        } else {
            LOGI("nativeGetParam can't get jstr of getPackageName");
        }
        LOGI("nativeGetParam ExceptionClear");
        (*env)->ExceptionClear(env);
        LOGI("nativeGetParam ReleaseStringUTFChars loc_str_app_packageName[%p]", loc_str_app_packageName);
        if (NULL != loc_str_app_packageName) {
            (*env)->ReleaseStringUTFChars(env, jstr_packageName, loc_str_app_packageName);
        }
        LOGI("nativeGetParam ReleaseStringUTFChars end");
    }
}

jstring getParam(JNIEnv * env, jobject thiz, jobject context, jobject headers, jobject params) {
    LOGI("nativeGetParam mSignatureStatus[%d]" , mSignatureStatus);
    jboolean hasException;

    checkPackage(env, thiz, context);

    LOGI("nativeGetParam mSignatureStatus[%d]" , mSignatureStatus);
    if (SIGNATURE_ILLEGAL != mSignatureStatus) {
        char *cValueString = NULL;
        // 签名合法的
        char* cHeaderString = getString(env, thiz, context, headers);
        char* cParamsString = NULL; // getString(env, thiz, context, params);
        LOGI("nativeGetParam cHeaderString[%p] cParamsString[%p]" , cHeaderString, cParamsString);
        if ((NULL != cHeaderString) && (strlen(cHeaderString) > 0)) {
            int paramLen = 0;
            if (NULL!= cParamsString) {
                paramLen = strlen(cParamsString);
            }
            int size = strlen(cHeaderString) + paramLen + strlen(global_ps) + 2;
            LOGI("nativeGetParam size[%d]" , size);
            char *temp  = malloc(size);
            strcpy(temp, cHeaderString);
            strcat(temp, "|");
            if (NULL != cParamsString) {
                strcat(temp, cParamsString);
            }
            strcat(temp, global_ps);
            free(cValueString);
            if (NULL != cParamsString) {
                free(cParamsString);
            }
            cValueString = temp;
            jclass clazz_util = (*env)->FindClass(env, global_util_class);
            LOGI("nativeGetParam clazz_util[%p]", clazz_util);
            if (!((*env)->ExceptionCheck(env) || NULL == clazz_util)) {
                jmethodID mid_md5 = (*env)->GetStaticMethodID(env, clazz_util, "getMD5Str", "(Ljava/lang/String;)Ljava/lang/String;");
                LOGI("nativeGetParam mid_md5[%p]", mid_md5);
                if (!((*env)->ExceptionCheck(env) || NULL == mid_md5)) {
                    LOGI("nativeGetParam cValueString[%s]", cValueString);
                    jstring result = (jstring)(*env)->CallStaticObjectMethod(env, clazz_util, mid_md5, (*env)->NewStringUTF(env, cValueString));
                    if (NULL != cValueString) {
                        free(cValueString);
                        cValueString = NULL;
                    }
                    (*env)->ExceptionClear(env);
                    return result;
                }
            }
        }
    } else {
        // 非法包
        int temp_size = strlen(global_invalid_signature) + 1;
        char *temp_str = malloc(temp_size);
        jstring result = NULL;
        if (NULL != temp_str) {
            strcpy(temp_str, global_invalid_signature);
            result = (jstring) (*env)->NewStringUTF(env, temp_str);
            free(temp_str);
            temp_str = NULL;
        }
        (*env)->ExceptionClear(env);
        return result;
    }
    LOGI("nativeGetParam ExceptionClear");
    (*env)->ExceptionClear(env);
    LOGI("nativeGetParam return");
    return NULL;
}

char* getString(JNIEnv * env, jobject thiz, jobject context, jobject params) {
    jboolean hasException;
    char *cValueString = NULL;
    LOGI("getString params[%p]", params);
    jclass clazz_comparator = (*env)->FindClass(env, global_comparator_class);
    LOGI("getString clazz_comparator[%p]", clazz_comparator);
    if ((NULL != params ) && !((*env)->ExceptionCheck(env) || NULL == clazz_comparator)) {
        jmethodID mid_comparator = (*env)->GetMethodID(env, clazz_comparator, "<init>", "()V");
        LOGI("getString mid_comparator[%p]", mid_comparator);
        if (!((*env)->ExceptionCheck(env) || NULL == mid_comparator)) {
            jobject comparator = (*env)->NewObject(env, clazz_comparator, mid_comparator);
            LOGI("getString comparator[%p]", comparator);
            if (!((*env)->ExceptionCheck(env) || NULL == comparator)) {
                jclass clazz_collections = (*env)->FindClass(env, "java/util/Collections");
                LOGI("getString clazz_collections[%p]", clazz_collections);
                if (!((*env)->ExceptionCheck(env) || (NULL == clazz_collections))) {
                    jmethodID mid_sort = (*env)->GetStaticMethodID(env, clazz_collections, "sort", "(Ljava/util/List;Ljava/util/Comparator;)V");
                    LOGI("getString mid_sort[%p]", mid_sort);
                    if (!((*env)->ExceptionCheck(env) || NULL == mid_sort)) {
                        (*env)->CallStaticVoidMethod(env, clazz_collections, mid_sort, params, comparator);
                        LOGI("getString sort end");
                        jclass cls_list = (*env)->GetObjectClass(env, params);
                        if (!((*env)->ExceptionCheck(env) || NULL == cls_list)) {
                            jmethodID list_get = (*env)->GetMethodID(env, cls_list, "get", "(I)Ljava/lang/Object;");
                            if (!((*env)->ExceptionCheck(env) || NULL == list_get)) {
                                jmethodID mid_encode = (*env)->GetStaticMethodID(env, clazz_comparator, "getEncodeString", "(Ljava/lang/String;)Ljava/lang/String;");
                                LOGI("getString mid_encode[%p]", mid_encode);
                                if (!((*env)->ExceptionCheck(env) || NULL == mid_encode)) {
                                    jint len = JNU_CallMethodByName(env, &hasException, params, "size", "()I").i;
                                    LOGI("getString len[%d]", len);
                                    int i = 0;
                                    for(i = 0; i < len; i++) {
                                        jobject item = JNU_CallMethodByName(env, &hasException, params, "get", "(I)Ljava/lang/Object;", i).l;
                                        // LOGI("getString i[%d] item[%p]", i, item);
                                        if (!((*env)->ExceptionCheck(env) || NULL == item)) {
                                            jstring name = JNU_CallMethodByName(env, &hasException, item, "getName", "()Ljava/lang/String;").l;
                                            jstring value =  JNU_CallMethodByName(env, &hasException, item, "getValue", "()Ljava/lang/String;").l;
                                            //LOGI("getString name[%p] value[%p]", name, value);
                                            if (!((*env)->ExceptionCheck(env) || NULL == name || NULL == value)) {
                                                jbyte *cName = (*env)->GetStringUTFChars(env, name, NULL);
                                                jbyte *cValue = (*env)->GetStringUTFChars(env, value, NULL);
                                                LOGI("getString cName[%s] cValue[%s]", cName, cValue);
                                                if ((strlen(cName) > 0) && (strlen(cValue) > 0)) {
                                                    jstring result = (jstring)(*env)->CallStaticObjectMethod(env, clazz_comparator, mid_encode, (*env)->NewStringUTF(env, cValue));
                                                    if (NULL != result) {
                                                        if (NULL != cValue) {
                                                            free(cValue);
                                                            cValue = NULL;
                                                        }
                                                        cValue = (*env)->GetStringUTFChars(env, result, NULL);
                                                        LOGI("getString cName[%s] encode cValue[%s]", cName, cValue);
                                                        int size = strlen(cName) + strlen(cValue) + 1;
                                                        if (size > 0) {
                                                            if (NULL != cValueString) {
                                                                size += strlen(cValueString) + 1;
                                                            }
                                                            LOGI("getString size[%d]", size);
                                                            char *temp  = malloc(size + 1);
                                                            LOGI("getString temp[%p]", temp);
                                                            if (NULL != cValueString) {
                                                                strcpy(temp, cValueString);
                                                                strcat(temp, "&");
                                                                strcat(temp, cName);
                                                                strcat(temp, "=");
                                                                strcat(temp, cValue);
                                                                free(cValueString);
                                                            } else {
                                                                strcpy(temp, cName);
                                                                strcat(temp, "=");
                                                                strcat(temp, cValue);
                                                            }
                                                            cValueString = temp;
                                                        }
                                                    }
                                                    if (NULL != cName) {
                                                        (*env)->ReleaseStringUTFChars(env, name, cName);
                                                    }
                                                    if (NULL != name) {
                                                        (*env)->DeleteLocalRef(env, name);
                                                    }
                                                    if (NULL != cValue) {
                                                        (*env)->ReleaseStringUTFChars(env, value, cValue);
                                                    }
                                                    if (NULL != value) {
                                                        (*env)->DeleteLocalRef(env, value);
                                                    }
                                                    if (NULL != item) {
                                                        (*env)->DeleteLocalRef(env, item);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    LOGI("getString ExceptionClear");
    (*env)->ExceptionClear(env);
    LOGI("getString cValueString[%p]", cValueString);
    return cValueString;
}

jvalue JNU_CallMethodByName(JNIEnv *env, jboolean *hasException, jobject obj, const char *name, const char *descriptor, ...) {
    va_list args;
    jclass clazz;
    jmethodID mid;
    jvalue result;
    if ((*env)->EnsureLocalCapacity(env, 2) == JNI_OK) {
        clazz = (*env)->GetObjectClass(env, obj);
        mid = (*env)->GetMethodID(env, clazz, name, descriptor);
        if (mid) {
            const char *p = descriptor;
            /* skip over argument types to find out the return type */
            while (*p != ')')
                p++;
            /* skip ')' */
            p++;
            va_start(args, descriptor);
            switch (*p) {
            case 'V':
                (*env)->CallVoidMethodV(env, obj, mid, args);
                break;
            case '[':
            case 'L':
                result.l = (*env)->CallObjectMethodV(env, obj, mid, args);
                break;
            case 'Z':
                result.z = (*env)->CallBooleanMethodV(env, obj, mid, args);
                break;
            case 'B':
                result.b = (*env)->CallByteMethodV(env, obj, mid, args);
                break;
            case 'C':
                result.c = (*env)->CallCharMethodV(env, obj, mid, args);
                break;
            case 'S':
                result.s = (*env)->CallShortMethodV(env, obj, mid, args);
                break;
            case 'I':
                result.i = (*env)->CallIntMethodV(env, obj, mid, args);
                break;
            case 'J':
                result.j = (*env)->CallLongMethodV(env, obj, mid, args);
                break;
            case 'F':
                result.f = (*env)->CallFloatMethodV(env, obj, mid, args);
                break;
            case 'D':
                result.d = (*env)->CallDoubleMethodV(env, obj, mid, args);
                break;
            default:
                (*env)->FatalError(env, "illegaldescriptor");
            }
            va_end(args);
        }
        (*env)->DeleteLocalRef(env, clazz);
    }
    if (hasException) {
        *hasException = (*env)->ExceptionCheck(env);
    }
    return result;
}