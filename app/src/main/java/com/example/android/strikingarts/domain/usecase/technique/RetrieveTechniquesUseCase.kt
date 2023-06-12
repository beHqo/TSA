package com.example.android.strikingarts.domain.usecase.technique

import com.example.android.strikingarts.domain.interfaces.TechniqueCacheRepository
import javax.inject.Inject

class RetrieveTechniquesUseCase @Inject constructor(repository: TechniqueCacheRepository) {
    val techniqueList = repository.techniqueList
}