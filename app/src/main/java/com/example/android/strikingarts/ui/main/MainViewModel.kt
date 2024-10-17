package com.example.android.strikingarts.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.strikingarts.domain.model.UserPreferences
import com.example.android.strikingarts.domain.userpreferences.RetrieveUserPreferencesFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(retrieveUserPreferencesFlowUseCase: RetrieveUserPreferencesFlowUseCase) :
    ViewModel() {
    private val _loadingScreen = MutableStateFlow(true)
    val loadingScreen = _loadingScreen.asStateFlow()

    val userPreferences = retrieveUserPreferencesFlowUseCase.data
        .onStart { _loadingScreen.update { false } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), UserPreferences())
}