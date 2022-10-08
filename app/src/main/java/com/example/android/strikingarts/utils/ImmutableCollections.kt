package com.example.android.strikingarts.utils

import androidx.compose.runtime.Immutable

@Immutable data class ImmutableList<T>(val list: List<T> = emptyList()) : List<T> by list
@Immutable data class ImmutableSet<T>(val set: Set<T> = emptySet()) : Set<T> by set
@Immutable data class ImmutableMap<K, V>(val map: Map<K, V> = emptyMap()) : Map<K, V> by map

fun <K, V> Map<K, V>.keysAsImmutableSet() = ImmutableSet(this.keys)
