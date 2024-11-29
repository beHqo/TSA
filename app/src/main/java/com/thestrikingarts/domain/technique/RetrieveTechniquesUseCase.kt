package com.thestrikingarts.domain.technique

import javax.inject.Inject

class RetrieveTechniquesUseCase @Inject constructor(repository: TechniqueCacheRepository) {
    val techniqueList = repository.techniqueList
}