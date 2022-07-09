package com.example.android.strikingarts.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.android.strikingarts.database.dao.ComboDao
import com.example.android.strikingarts.database.dao.TechniqueDao
import com.example.android.strikingarts.database.dao.WorkoutDao
import com.example.android.strikingarts.database.entity.Combo
import com.example.android.strikingarts.database.entity.ComboTechniqueCrossRef
import com.example.android.strikingarts.database.entity.Technique
import com.example.android.strikingarts.database.entity.Workout

@Database(
    entities = [Technique::class, Combo::class, Workout::class, ComboTechniqueCrossRef::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(MovementConverter::class, TechniqueConverter::class)
abstract class StrikingDatabase : RoomDatabase() {

    abstract fun techniqueDao(): TechniqueDao
    abstract fun comboDao(): ComboDao
    abstract fun workoutDao(): WorkoutDao

}
