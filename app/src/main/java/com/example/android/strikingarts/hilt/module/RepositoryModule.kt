package com.example.android.strikingarts.hilt.module

import com.example.android.strikingarts.data.repository.ComboRepository
import com.example.android.strikingarts.data.repository.TechniqueRepository
import com.example.android.strikingarts.data.repository.WorkoutRepository
import com.example.android.strikingarts.domain.interfaces.ComboCacheRepository
import com.example.android.strikingarts.domain.interfaces.TechniqueCacheRepository
import com.example.android.strikingarts.domain.interfaces.WorkoutCacheRepository
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
}