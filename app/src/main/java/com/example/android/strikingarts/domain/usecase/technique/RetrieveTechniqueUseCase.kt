package com.example.android.strikingarts.domain.usecase.technique

import com.example.android.strikingarts.domain.model.TechniqueListItem
import com.example.android.strikingarts.domain.repository.TechniqueCacheRepository
import javax.inject.Inject

class RetrieveTechniqueUseCase @Inject constructor(private val repository: TechniqueCacheRepository) {
    suspend operator fun invoke(id: Long): TechniqueListItem = repository.getTechnique(id)
}