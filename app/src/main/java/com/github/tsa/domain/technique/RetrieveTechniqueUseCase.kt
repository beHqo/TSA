package com.github.tsa.domain.technique

import com.github.tsa.domain.model.Technique
import javax.inject.Inject

class RetrieveTechniqueUseCase @Inject constructor(private val repository: TechniqueCacheRepository) {
    suspend operator fun invoke(id: Long): Technique = repository.getTechnique(id)
}