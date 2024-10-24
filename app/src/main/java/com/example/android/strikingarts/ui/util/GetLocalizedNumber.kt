package com.example.android.strikingarts.ui.util

import java.util.Locale

fun Int.localized(): String = String.format(Locale.getDefault(), "%d", this)