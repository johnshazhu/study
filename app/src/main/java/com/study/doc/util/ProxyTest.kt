package com.study.doc.util

interface Animal {
    fun bark()
}

class Dog : Animal {
    override fun bark() {
        println("wang")
    }
}

class Zoo(animal: Animal) : Animal by animal