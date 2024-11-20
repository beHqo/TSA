package com.github.tsa.domain.combo

import com.github.tsa.domain.model.Combo
import javax.inject.Inject

class RetrieveComboUseCase @Inject constructor(private val repository: ComboCacheRepository) {
    suspend operator fun invoke(id: Long): Combo = repository.getCombo(id)
}