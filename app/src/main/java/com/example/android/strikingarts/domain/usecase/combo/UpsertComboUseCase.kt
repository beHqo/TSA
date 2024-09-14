package com.example.android.strikingarts.domain.usecase.combo

import com.example.android.strikingarts.domain.interfaces.ComboCacheRepository
import com.example.android.strikingarts.domain.model.Combo
import javax.inject.Inject

class UpsertComboUseCase @Inject constructor(private val repository: ComboCacheRepository) {
    suspend operator fun invoke(combo: Combo, techniqueIdList: List<Long>) {
        if (combo.id == 0L) repository.insert(combo, techniqueIdList)
        else repository.update(combo, techniqueIdList)
    }
}