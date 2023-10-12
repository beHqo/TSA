package com.example.android.strikingarts.domain.usecase.userpreferences

import com.example.android.strikingarts.domain.interfaces.UserPreferencesRepository
import javax.inject.Inject

class RetrieveUserPreferencesFlowUseCase @Inject constructor(repository: UserPreferencesRepository) {
    val data = repository.userPreferencesFlow
}