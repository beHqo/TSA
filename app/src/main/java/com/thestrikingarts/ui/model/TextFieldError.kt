package com.thestrikingarts.ui.model

import androidx.annotation.StringRes

data class TextFieldError(@StringRes val messageId: Int, val predicate: (String) -> Boolean)