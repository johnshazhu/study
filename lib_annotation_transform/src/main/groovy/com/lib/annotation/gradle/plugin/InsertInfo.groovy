package com.lib.annotation.gradle.plugin

import javassist.CtField
import javassist.CtMethod

class InsertInfo {
    CtMethod destMtd
    CtMethod srcMtd
    CtField destField
    CtField srcField
    String ctClassName
}