package com.example.android.strikingarts.domain.usecase.technique

import com.example.android.strikingarts.domain.repository.TechniqueCacheRepository
import javax.inject.Inject

class DeleteTechniqueUseCase @Inject constructor(private val repository: TechniqueCacheRepository) {
    suspend operator fun invoke(id: Long) = repository.delete(id)

    suspend operator fun invoke(idList: List<Long>) = repository.deleteAll(idList)
}