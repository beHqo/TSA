package com.example.android.strikingarts.utils

import androidx.compose.runtime.Immutable

@Immutable data class ImmutableList<T>(val list: List<T> = emptyList()) : List<T> by list