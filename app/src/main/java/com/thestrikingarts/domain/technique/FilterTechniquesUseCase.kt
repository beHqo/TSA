package com.thestrikingarts.domain.technique

import com.thestrikingarts.di.DefaultDispatcher
import com.thestrikingarts.domain.model.MovementType
import com.thestrikingarts.domain.model.Technique
import com.thestrikingarts.domain.model.TechniqueType
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
    private val movementType = MutableStateFlow(MovementType.OFFENSE)
    private val techniqueType = MutableStateFlow(TechniqueType.UNSPECIFIED)

    val techniqueList =
        combine(retrieveTechniquesUseCase.techniqueList, techniqueType, movementType) { list, tt, mt ->
            list.filter { technique -> filteringPredicate(technique, tt, mt) }
        }.flowOn(defaultDispatcher)

    private fun filteringPredicate(
        technique: Technique, techniqueType: TechniqueType, movementType: MovementType
    ) = when {
        techniqueType == TechniqueType.UNSPECIFIED -> technique.movementType == movementType
        else -> technique.movementType == movementType && technique.techniqueType == techniqueType
    }

    fun setTechniqueType(techniqueType: TechniqueType) {
        this.techniqueType.update { techniqueType }
    }

    fun setMovementType(movementType: MovementType) {
        this.movementType.update { movementType }
    }
}