package com.github.tsa.di

import com.github.tsa.data.repository.AudioAttributesRepository
import com.github.tsa.data.repository.ComboRepository
import com.github.tsa.data.repository.TechniqueRepository
import com.github.tsa.data.repository.WorkoutRepository
import com.github.tsa.data.repository.WorkoutResultRepository
import com.github.tsa.domain.audioattributes.AudioAttributesCacheRepository
import com.github.tsa.domain.combo.ComboCacheRepository
import com.github.tsa.domain.technique.TechniqueCacheRepository
import com.github.tsa.domain.workout.WorkoutCacheRepository
import com.github.tsa.domain.workoutresult.WorkoutResultCacheRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun bindsTechniqueCacheRepository(techniqueRepository: TechniqueRepository): TechniqueCacheRepository

    @Binds
    fun bindsComboCacheRepository(comboRepository: ComboRepository): ComboCacheRepository

    @Binds
    fun bindsWorkoutCacheRepository(workoutRepository: WorkoutRepository): WorkoutCacheRepository

    @Binds
    fun bindsWorkoutResultCacheRepository(workoutConclusionRepository: WorkoutResultRepository): WorkoutResultCacheRepository

    @Binds
    fun bindsAudioAttributesCacheRepository(audioAttributesRepository: AudioAttributesRepository): AudioAttributesCacheRepository
}