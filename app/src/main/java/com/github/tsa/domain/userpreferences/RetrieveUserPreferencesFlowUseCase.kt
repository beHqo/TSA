package com.github.tsa.domain.userpreferences

import javax.inject.Inject

class RetrieveUserPreferencesFlowUseCase @Inject constructor(repository: UserPreferencesRepository) {
    val data = repository.userPreferencesFlow
}