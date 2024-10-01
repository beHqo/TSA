package com.example.android.strikingarts.di

import com.example.android.strikingarts.data.repository.AudioAttributesRepository
import com.example.android.strikingarts.data.repository.ComboRepository
import com.example.android.strikingarts.data.repository.TechniqueRepository
import com.example.android.strikingarts.data.repository.WorkoutRepository
import com.example.android.strikingarts.data.repository.WorkoutResultRepository
import com.example.android.strikingarts.domain.audioattributes.AudioAttributesCacheRepository
import com.example.android.strikingarts.domain.combo.ComboCacheRepository
import com.example.android.strikingarts.domain.technique.TechniqueCacheRepository
import com.example.android.strikingarts.domain.workout.WorkoutCacheRepository
import com.example.android.strikingarts.domain.workoutresult.WorkoutResultCacheRepository
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