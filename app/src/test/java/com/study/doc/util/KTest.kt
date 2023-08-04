package com.study.doc.util

import junit.framework.TestCase

class KTest : TestCase() {
    inline fun <reified T> membersOf() = T::class.members

    fun testMember(s: Array<String>) {
        println(membersOf<StringBuilder>().joinToString("\n"))
    }

    fun testMain() {
        testMember(Array(3) {
            "$it"
        })
    }

    fun testNoInline() {
        val str = null
        val str1 = "Hello"
        testInline(str,
            {
                println("$it not null")
                str1
            },
            {
                if (it == null) {
                println("$it is null")
                } else {
                    println("$it")
                }
            }
        )

        testInline(str1,
            {
                println("$it not null")
                str1
            },
            {
                if (it == null) {
                    println("$it is null")
                } else {
                    println("$it")
                }
            }
        )
    }

    inline fun testInline(string: String?,
                                  block: (String?) -> String,
                                  noinline block_noinline: (String?) -> Unit) {
        string?.let(block)

        block_noinline(string)

        // Public-API inline function cannot access non-public-API 'private final fun hello():
//        hello()
    }

    private fun hello() {
        println("hello")
    }

    fun testNonLocalReturn() {
        val ints = listOf(1, 2, 0, 3)
        foo(ints)
    }

    fun foo(ints: List<Int>): Boolean {
        ints.forEach {
            // 'break' and 'continue' are only allowed inside a loop
//            if (it == 0) break
//            if (it == 0) return@forEach
            if (it == 0) return true
            println(it)
        }
        println("return false")
        return false
    }

    fun testLoop() {
        for (i in 1..10) {
            if (i == 5) {
                break
            }
            println(i)
        }
        f {
            println("body")
        }
    }

    // Can't inline 'body' here: it may contain non-local returns.
    // Add 'crossinline' modifier to parameter declaration 'body'
    inline fun f(crossinline body: () -> Unit) {
        val r = object : Runnable {
            override fun run() {
                body()
            }
        }
        r.run()
    }
}