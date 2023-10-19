package com.example.android.strikingarts.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.android.strikingarts.data.local.room.dao.ComboDao
import com.example.android.strikingarts.data.local.room.dao.TechniqueDao
import com.example.android.strikingarts.data.local.room.dao.TrainingDateDao
import com.example.android.strikingarts.data.local.room.dao.WorkoutConclusionDao
import com.example.android.strikingarts.data.local.room.dao.WorkoutDao
import com.example.android.strikingarts.data.local.room.model.Combo
import com.example.android.strikingarts.data.local.room.model.ComboTechniqueCrossRef
import com.example.android.strikingarts.data.local.room.model.Technique
import com.example.android.strikingarts.data.local.room.model.TrainingDate
import com.example.android.strikingarts.data.local.room.model.Workout
import com.example.android.strikingarts.data.local.room.model.WorkoutComboCrossRef
import com.example.android.strikingarts.data.local.room.model.WorkoutConclusion
import com.example.android.strikingarts.data.local.room.typeconverters.DataAudioAttributesTypeConverter

@Database(
    entities = [Technique::class, Combo::class, Workout::class, ComboTechniqueCrossRef::class, WorkoutComboCrossRef::class, TrainingDate::class, WorkoutConclusion::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(value = [DataAudioAttributesTypeConverter::class])
abstract class StrikingDatabase : RoomDatabase() {

    abstract fun techniqueDao(): TechniqueDao
    abstract fun comboDao(): ComboDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun trainingDateDao(): TrainingDateDao
    abstract fun workoutConclusionDao(): WorkoutConclusionDao

}