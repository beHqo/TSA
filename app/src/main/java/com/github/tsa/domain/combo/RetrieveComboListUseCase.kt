package com.github.tsa.domain.combo

import javax.inject.Inject

class RetrieveComboListUseCase @Inject constructor(comboRepository: ComboCacheRepository) {
    val comboList = comboRepository.comboList
}