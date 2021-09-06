cmake_minimum_required(VERSION 3.4.1)

set(INC_DIR ${PROJECT_SOURCE_DIR}/src/main/jni)
set(LINK_DIR ${PROJECT_SOURCE_DIR}/libs/armeabi-v7a)
# set(LINK_DIR ${PROJECT_SOURCE_DIR}/libs/${ANDROID_ABI})

# 添加so库对应的头文件目录
include_directories(${INC_DIR})

add_library(ffmpeg
        SHARED IMPORTED)
# 设置编译的库文件存放的目录
set_target_properties(ffmpeg
        PROPERTIES IMPORTED_LOCATION
        ${CMAKE_CURRENT_SOURCE_DIR}/libs/armeabi-v7a/libffmpeg.so)

add_library(native_lib
        SHARED
        src/main/jni/native_lib.cpp)

find_library(log-lib
        log)

target_link_libraries(native_lib
 ffmpeg
 android
 log)