package com.example.android.strikingarts.ui.combo

import androidx.lifecycle.ViewModel
import com.example.android.strikingarts.database.repository.ComboRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ComboViewModel @Inject constructor(comboRepository: ComboRepository) : ViewModel() {
    val comboList = comboRepository.comboList

}