package com.github.tsa.data.local.util

import com.github.tsa.data.local.dao.AudioAttributesDao
import com.github.tsa.data.local.dao.ComboDao
import com.github.tsa.data.local.dao.TechniqueDao
import com.github.tsa.domain.model.Combo
import com.github.tsa.domain.model.UriAudioAttributes

class InsertCombo {
    suspend operator fun invoke(
        combo: Combo,
        audioAttributesDao: AudioAttributesDao,
        techniqueDao: TechniqueDao,
        comboDao: ComboDao
    ): Long {
        val techniqueIdList = mutableListOf<Long>()

        combo.techniqueList.forEach { technique ->
            var audioAttributesId: Long? = null

            technique.audioAttributes.let {
                if (it is UriAudioAttributes) audioAttributesId = audioAttributesDao.insert(it)
            }

            techniqueIdList += techniqueDao.insert(technique, audioAttributesId)
        }

        return comboDao.insert(combo, techniqueIdList)
    }
}