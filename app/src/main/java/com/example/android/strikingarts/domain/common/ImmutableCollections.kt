package com.example.android.strikingarts.domain.common

import androidx.compose.runtime.Immutable

@Immutable data class ImmutableList<T>(val list: List<T> = emptyList()) : List<T> by list
@Immutable data class ImmutableSet<T>(val set: Set<T> = emptySet()) : Set<T> by set