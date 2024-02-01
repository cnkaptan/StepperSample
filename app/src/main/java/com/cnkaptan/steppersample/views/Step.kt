package com.cnkaptan.steppersample.views

import androidx.annotation.StringRes
import com.cnkaptan.steppersample.R

enum class StepState {
    COMPLETED, INCOMPLETE, CURRENT
}

data class Step(val id: Int, @StringRes val textId: Int, val stepState: StepState = StepState.INCOMPLETE)

val MOCK_STEPS = listOf(
    Step(id = 1, textId = R.string.step_1, stepState = StepState.COMPLETED),
    Step(id = 2,textId = R.string.step_2, stepState = StepState.COMPLETED),
    Step(id = 3,textId = R.string.step_1, stepState = StepState.CURRENT),
    Step(id = 4,textId = R.string.step_2, stepState = StepState.INCOMPLETE),
    Step(id = 5,textId = R.string.step_1, stepState = StepState.INCOMPLETE),
    Step(id = 6,textId = R.string.step_2, stepState = StepState.INCOMPLETE),
    Step(id = 7,textId = R.string.step_1, stepState = StepState.INCOMPLETE),
)