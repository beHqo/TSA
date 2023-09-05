package com.example.android.strikingarts.domain.interfaces

import com.example.android.strikingarts.domain.model.ComboListItem
import com.example.android.strikingarts.domain.model.ImmutableList
import kotlinx.coroutines.flow.Flow

interface ComboCacheRepository {
    val comboList: Flow<ImmutableList<ComboListItem>>

    suspend fun getCombo(id: Long): ComboListItem
    suspend fun insert(comboListItem: ComboListItem)
    suspend fun update(comboListItem: ComboListItem)
    suspend fun delete(id: Long)
    suspend fun deleteAll(idList: List<Long>)
    suspend fun insertComboTechniqueCrossRef(
        comboListItem: ComboListItem, techniqueIdList: List<Long>
    )

    suspend fun updateComboTechniqueCrossRef(
        comboListItem: ComboListItem, techniqueIdList: List<Long>
    )
}