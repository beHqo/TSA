package com.example.android.strikingarts.domain.usecase.technique

import com.example.android.strikingarts.domain.interfaces.TechniqueCacheRepository
import com.example.android.strikingarts.domain.model.TechniqueListItem
import javax.inject.Inject

class UpsertTechniqueUseCase @Inject constructor(
    private val repository: TechniqueCacheRepository
) {
    suspend operator fun invoke(techniqueListItem: TechniqueListItem) {
        if (techniqueListItem.id == 0L) repository.insert(techniqueListItem)
        else repository.update(techniqueListItem)
    }
}