package com.thestrikingarts.ui.util

import java.util.Locale

fun Int.localized(): String = String.format(Locale.getDefault(), "%d", this)