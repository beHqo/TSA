package com.example.android.strikingarts.domain.usecase.technique

import com.example.android.strikingarts.domain.interfaces.TechniqueCacheRepository
import com.example.android.strikingarts.domain.model.TechniqueListItem
import javax.inject.Inject

class RetrieveTechniqueUseCase @Inject constructor(private val repository: TechniqueCacheRepository) {
    suspend operator fun invoke(id: Long): TechniqueListItem = repository.getTechnique(id)
}