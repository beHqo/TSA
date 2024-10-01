package com.example.android.strikingarts.domain.technique

import com.example.android.strikingarts.domain.model.Technique
import javax.inject.Inject

class RetrieveTechniqueUseCase @Inject constructor(private val repository: TechniqueCacheRepository) {
    suspend operator fun invoke(id: Long): Technique = repository.getTechnique(id)
}