package io.rybalkinsd.kotlinbootcamp.util

import kotlin.reflect.KProperty

class User {
    var name = "Alice"
    val age by ageDelegate()
}

class ageDelegate {
    var age: Int? = null
    operator fun getValue(thisRef: Any?, property: KProperty<*>): Int = age!!

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
        if (value < 0) age = 0
        else if (value > 120) age = 120
    }
}