cmake_minimum_required(VERSION 3.4.1)
add_library(api_encrypt
 SHARED
 api_encrypt.c
 util.c)
target_link_libraries(api_encrypt
 android
 log)