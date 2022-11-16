package com.example.android.strikingarts.database.repository

import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SelectedItemsRepository @Inject constructor() {
    val selectedIds: MutableStateFlow<List<Long>> = MutableStateFlow(listOf())
}