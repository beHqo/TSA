package com.example.android.strikingarts.domain.usecase.technique

import com.example.android.strikingarts.domain.common.ImmutableList
import com.example.android.strikingarts.domain.model.TechniqueCategory.OFFENSE
import com.example.android.strikingarts.domain.model.TechniqueListItem
import com.example.android.strikingarts.hilt.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class FilterTechniquesUseCase @Inject constructor(
    retrieveTechniquesUseCase: RetrieveTechniquesUseCase,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) {
    private val movementType = MutableStateFlow(OFFENSE)
    private val techniqueType = MutableStateFlow("")

    val techniqueList =
        combine(retrieveTechniquesUseCase.techniqueList, techniqueType, movementType) { list, tt, mt ->
            ImmutableList(list.filter { technique -> filteringPredicate(technique, tt, mt) })
        }.flowOn(defaultDispatcher)

    private fun filteringPredicate(
        technique: TechniqueListItem, techniqueType: String, movementType: String
    ) = if (techniqueType.isEmpty()) technique.movementType == movementType
    else if (movementType.isEmpty()) technique.techniqueType == techniqueType
    else if (movementType.isEmpty() && techniqueType.isEmpty()) technique.movementType == OFFENSE
    else technique.techniqueType == techniqueType && technique.movementType == movementType

    fun setTechniqueType(techniqueType: String) {
        this.techniqueType.update { techniqueType }
    }

    fun setMovementType(movementType: String) {
        this.movementType.update { movementType }
    }
}