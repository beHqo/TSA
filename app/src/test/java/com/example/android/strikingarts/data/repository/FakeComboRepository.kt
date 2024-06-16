package com.example.android.strikingarts.data.repository

import com.example.android.strikingarts.data.listOfCombos
import com.example.android.strikingarts.domain.interfaces.ComboCacheRepository
import com.example.android.strikingarts.domain.model.Combo
import com.example.android.strikingarts.domain.model.ImmutableList
import com.example.android.strikingarts.domain.model.Technique
import com.example.android.strikingarts.domain.model.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeComboRepository : ComboCacheRepository {
    private var lastAvailableIndex = -1L
    private val data = listOfCombos.toMutableList()

    init {
        lastAvailableIndex = (data.maxOfOrNull { it.id } ?: 0) + 1
    }

    override val comboList: Flow<ImmutableList<Combo>> = flowOf(data.toImmutableList())

    override suspend fun getCombo(id: Long): Combo = data.firstOrNull { it.id == id } ?: Combo()

    override suspend fun insert(comboListItem: Combo, techniqueIdList: List<Long>) {
        data += comboListItem.copy(id = lastAvailableIndex++,
            techniqueList = techniqueIdList.map { Technique(id = it, name = "Technique $it") }
                .toImmutableList())
    }

    override suspend fun update(comboListItem: Combo, techniqueIdList: List<Long>) {
        val retrieved = data.firstOrNull { it.id == comboListItem.id } ?: return

        data -= retrieved

        data += comboListItem.copy(techniqueList = techniqueIdList.map {
            Technique(id = it, name = "Technique $it")
        }.toImmutableList())
    }

    override suspend fun delete(id: Long) {
        data.removeIf { it.id == id }
    }

    override suspend fun deleteAll(idList: List<Long>) {
        data.removeIf { it.id in idList }
    }

    fun doesDatabaseContainComboWithIdOf(id: Long): Boolean = data.any { it.id == id }

    fun getLastInsertedCombo(): Combo = data.last()
}