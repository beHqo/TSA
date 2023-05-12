package com.example.android.strikingarts.ui.model

import androidx.annotation.StringRes

data class TextFieldError(@StringRes val messageId: Int, val predicate: (String) -> Boolean)