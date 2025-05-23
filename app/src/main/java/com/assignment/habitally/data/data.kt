package com.assignment.habitally.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AppState: ViewModel () {
    var waterCount by mutableIntStateOf(0)
        private set
    var waterTargetCount by mutableIntStateOf(0)
        private set
    var waterDaysCompleted by mutableIntStateOf(0)
        private set

    var workoutMinutes by mutableIntStateOf(0)
        private set
    var workoutActivities by mutableIntStateOf(0)
        private set

    var workoutTargetMinutes by mutableIntStateOf(0)
        private set
    var workoutTargetActivities by mutableIntStateOf(0)
        private set
    var workoutDaysCompleted by mutableIntStateOf(0)
        private set

    var workoutWeeklyMinutes by mutableIntStateOf(0)
        private set
    var workoutWeeklyActivities by mutableIntStateOf(0)
        private set

    fun updateWaterCount (data: Int, type: Int) {
        waterCount = if (type == 1) {
            waterCount + data
        } else if (type == 2) {
            waterCount - data
        } else {
            0
        }
        if (waterCount >= waterTargetCount) {waterDaysCompleted = 1}
    }
    fun updateWorkoutData (minutes: Int) {
        workoutActivities++
        workoutWeeklyActivities++
        workoutMinutes = workoutMinutes + minutes
        workoutWeeklyMinutes = workoutMinutes
        if (workoutMinutes >= workoutTargetMinutes && workoutActivities >= workoutTargetActivities) {workoutDaysCompleted = 1}
    }
    fun updateWaterTarget (newTarget: Int) {
        waterTargetCount = newTarget
        if (waterCount >= waterTargetCount) {waterDaysCompleted = 1}
    }
    fun updateWorkoutTarget (newTargetMinutes: Int, newTargetActivities: Int) {
        workoutTargetActivities = newTargetActivities
        workoutTargetMinutes = newTargetMinutes
        if (workoutMinutes >= workoutTargetMinutes && workoutActivities >= workoutTargetActivities) {workoutDaysCompleted = 1}
    }
}