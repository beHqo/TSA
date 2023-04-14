package com.example.android.strikingarts.domain.usecase.combo

import com.example.android.strikingarts.domain.model.ComboListItem
import com.example.android.strikingarts.domain.repository.ComboCacheRepository
import javax.inject.Inject

class RetrieveComboUseCase @Inject constructor(private val repository: ComboCacheRepository) {
    suspend operator fun invoke(id: Long): ComboListItem = repository.getCombo(id)
}