package com.example.android.strikingarts.domain.usecase.combo

import com.example.android.strikingarts.domain.repository.ComboCacheRepository
import javax.inject.Inject

class DeleteComboUseCase @Inject constructor(private val repository: ComboCacheRepository) {
    suspend operator fun invoke(id: Long) = repository.delete(id)

    suspend operator fun invoke(idList: List<Long>) = repository.deleteAll(idList)
}