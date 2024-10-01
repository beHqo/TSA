package com.example.android.strikingarts.domain.combo

import com.example.android.strikingarts.domain.model.Combo
import javax.inject.Inject

class RetrieveComboUseCase @Inject constructor(private val repository: ComboCacheRepository) {
    suspend operator fun invoke(id: Long): Combo = repository.getCombo(id)
}