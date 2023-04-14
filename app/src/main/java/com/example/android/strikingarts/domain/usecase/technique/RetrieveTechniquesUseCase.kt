package com.example.android.strikingarts.domain.usecase.technique

import com.example.android.strikingarts.domain.repository.TechniqueCacheRepository
import javax.inject.Inject

class RetrieveTechniquesUseCase @Inject constructor(repository: TechniqueCacheRepository) {
    val techniqueList = repository.techniqueList
}