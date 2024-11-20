package com.github.tsa.domain.model

sealed class WorkoutConclusion {
    data object Successful : WorkoutConclusion()
    data class Aborted(val redeemed: Boolean = false) : WorkoutConclusion()

    fun isWorkoutFailed(): Boolean = this is Aborted && !this.redeemed
}