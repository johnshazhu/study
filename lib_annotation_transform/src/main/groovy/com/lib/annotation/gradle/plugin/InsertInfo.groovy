package com.lib.annotation.gradle.plugin

import javassist.CtClass
import javassist.CtField
import javassist.CtMethod

class InsertInfo {
    CtMethod destMtd
    CtMethod srcMtd
    CtClass[] param
    boolean isInterface
    CtField destField
    CtField srcField
    String ctClassName
}