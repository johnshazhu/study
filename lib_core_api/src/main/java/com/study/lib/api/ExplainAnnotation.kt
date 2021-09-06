package com.study.lib.api

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@Retention(RetentionPolicy.SOURCE)
@Target(AnnotationTarget.FIELD)
annotation class ExplainAnnotation(val desc: String = "")