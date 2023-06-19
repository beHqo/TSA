package com.example.android.strikingarts.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.android.strikingarts.data.local.room.dao.ComboDao
import com.example.android.strikingarts.data.local.room.dao.TechniqueDao
import com.example.android.strikingarts.data.local.room.dao.WorkoutDao
import com.example.android.strikingarts.data.local.room.model.Combo
import com.example.android.strikingarts.data.local.room.model.ComboTechniqueCrossRef
import com.example.android.strikingarts.data.local.room.model.Technique
import com.example.android.strikingarts.data.local.room.model.Workout
import com.example.android.strikingarts.data.local.room.model.WorkoutComboCrossRef

@Database(
    entities = [Technique::class, Combo::class, Workout::class, ComboTechniqueCrossRef::class, WorkoutComboCrossRef::class],
    version = 1,
    exportSchema = true
)
abstract class StrikingDatabase : RoomDatabase() {

    abstract fun techniqueDao(): TechniqueDao
    abstract fun comboDao(): ComboDao
    abstract fun workoutDao(): WorkoutDao

}