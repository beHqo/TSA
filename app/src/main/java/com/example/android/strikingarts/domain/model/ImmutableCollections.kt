package com.example.android.strikingarts.domain.model

import androidx.compose.runtime.Immutable

@Immutable data class ImmutableList<T>(val list: List<T> = emptyList()) : List<T> by list
@Immutable data class ImmutableSet<T>(val set: Set<T> = emptySet()) : Set<T> by set

fun <T> List<T>.toImmutableList() = ImmutableList(this)
fun IntRange.toImmutableList() = ImmutableList(this.toList())