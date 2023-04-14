package com.example.android.strikingarts.domain.usecase.combo

import com.example.android.strikingarts.domain.repository.ComboCacheRepository
import javax.inject.Inject

class RetrieveComboListUseCase @Inject constructor(comboRepository: ComboCacheRepository) {
    val comboList = comboRepository.comboList
}