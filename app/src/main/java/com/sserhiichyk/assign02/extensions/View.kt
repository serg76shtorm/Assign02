package com.sserhiichyk.assign02.extensions

import android.content.SharedPreferences
import android.view.View

private const val ARRAY_SEPARATOR = ";"

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun SharedPreferences.Editor.putIntArray(key: String, value: IntArray): SharedPreferences.Editor {
    return putString(
        key, value.joinToString(
            separator = ";",
            transform = { it.toString() })
    )
}

fun SharedPreferences.getIntArray(key: String): ArrayList<Int> {
    val intArray = ArrayList<Int>()
    val arrayInt = key.split(';')

    arrayInt.forEach {
        intArray.add(it.toInt())
    }

    return intArray
}