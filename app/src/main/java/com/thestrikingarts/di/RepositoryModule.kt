package com.thestrikingarts.di

import com.thestrikingarts.data.repository.AudioAttributesRepository
import com.thestrikingarts.data.repository.ComboRepository
import com.thestrikingarts.data.repository.TechniqueRepository
import com.thestrikingarts.data.repository.WorkoutRepository
import com.thestrikingarts.data.repository.WorkoutResultRepository
import com.thestrikingarts.domain.audioattributes.AudioAttributesCacheRepository
import com.thestrikingarts.domain.combo.ComboCacheRepository
import com.thestrikingarts.domain.technique.TechniqueCacheRepository
import com.thestrikingarts.domain.workout.WorkoutCacheRepository
import com.thestrikingarts.domain.workoutresult.WorkoutResultCacheRepository
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