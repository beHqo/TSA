package com.github.tsa.domain.mapper

fun Int.toMilliSeconds(): Long = this * 1000L
fun Long.toSeconds(): Int = (this / 1000).toInt()