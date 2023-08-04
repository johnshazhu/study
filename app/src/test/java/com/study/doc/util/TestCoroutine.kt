package com.study.doc.util

import junit.framework.TestCase

import kotlinx.coroutines.*
import java.util.*

class TestCoroutine : TestCase() {
    fun main() = runBlocking { // this: CoroutineScope
        launch { // launch a new coroutine and continue
            delay(1000L) // non-blocking delay for 1 second (default time unit is ms)
            println("World!") // print after delay
        }
        println("Hello") // main coroutine continues while a previous one is delayed
    }

    fun testCoroute() {
        val map = HashMap<Int, Boolean>()
        for (i in 1..10) {
            map.put(i, i % 2 == 0)
        }
    }
}