package com.example.android.strikingarts.database

import androidx.room.TypeConverter
import com.example.android.strikingarts.database.entity.MovementType
import com.example.android.strikingarts.database.entity.TechniqueType

class MovementConverter {
    @TypeConverter
    fun toMovementType(movementString: String) = enumValueOf<MovementType>(movementString)
    @TypeConverter
    fun fromMovementType(movementType: MovementType) = movementType.name
}

class TechniqueConverter {
    @TypeConverter
    fun toTechniqueType(techniqueString: String) = enumValueOf<TechniqueType>(techniqueString)
    @TypeConverter
    fun fromTechniqueType(techniqueType: TechniqueType) = techniqueType.name
}