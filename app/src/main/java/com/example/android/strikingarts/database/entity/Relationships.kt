package com.example.android.strikingarts.database.entity

import androidx.room.*

//@Entity(primaryKeys = ["comboId", "techniqueId"])
@Entity(tableName = "combo_technique_ref")
data class ComboTechniqueCrossRef(
    @PrimaryKey(autoGenerate = true)
    var refId: Long = 0,
    val comboId: Long,
    val techniqueId: Long,
)

data class ComboWithTechniques(
    @Embedded
    val combo: Combo,
    @Relation(
        parentColumn = "comboId",
        entityColumn = "techniqueId",
        associateBy = Junction(ComboTechniqueCrossRef::class)
    )
    val techniques: List<Technique>
)

data class WorkoutsWithCombos(
    @Embedded
    val workout: Workout,
    @Relation(
        entity = ComboWithTechniques::class,
        parentColumn = "workoutId",
        entityColumn = "comboId"
    )
    val combos: List<Combo>
)
