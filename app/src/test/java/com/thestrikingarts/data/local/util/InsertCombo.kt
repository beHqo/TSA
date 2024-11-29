package com.thestrikingarts.data.local.util

import com.thestrikingarts.data.local.dao.AudioAttributesDao
import com.thestrikingarts.data.local.dao.ComboDao
import com.thestrikingarts.data.local.dao.TechniqueDao
import com.thestrikingarts.domain.model.Combo
import com.thestrikingarts.domain.model.UriAudioAttributes

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