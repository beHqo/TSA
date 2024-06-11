package com.example.android.strikingarts.hilt.module

import com.example.android.strikingarts.data.repository.AudioAttributesRepository
import com.example.android.strikingarts.data.repository.ComboRepository
import com.example.android.strikingarts.data.repository.TechniqueRepository
import com.example.android.strikingarts.data.repository.WorkoutRepository
import com.example.android.strikingarts.data.repository.WorkoutResultRepository
import com.example.android.strikingarts.domain.interfaces.AudioAttributesCacheRepository
import com.example.android.strikingarts.domain.interfaces.ComboCacheRepository
import com.example.android.strikingarts.domain.interfaces.TechniqueCacheRepository
import com.example.android.strikingarts.domain.interfaces.WorkoutCacheRepository
import com.example.android.strikingarts.domain.interfaces.WorkoutResultCacheRepository
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
    fun bindsWorkoutConclusionCacheRepository(workoutConclusionRepository: WorkoutResultRepository): WorkoutResultCacheRepository

    @Binds
    fun bindsAudioAttributesCacheRepository(audioAttributesRepository: AudioAttributesRepository): AudioAttributesCacheRepository
}