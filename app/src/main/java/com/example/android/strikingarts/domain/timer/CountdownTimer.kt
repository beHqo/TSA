package com.example.android.strikingarts.domain.timer

import com.example.android.strikingarts.hilt.module.DefaultDispatcher
import com.example.android.strikingarts.ui.model.TimerState
import com.example.android.strikingarts.ui.model.TimerStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class CountdownTimer @Inject constructor(
    @DefaultDispatcher private val defaultDispatchers: CoroutineDispatcher
) {
    private var remainingTimeSeconds = 0

    @OptIn(ExperimentalCoroutinesApi::class)
    val timerFlow: (timerState: StateFlow<TimerState>) -> Flow<Int> = { timerState ->
        timerState.flatMapLatest { state ->
            flow {
                when (state.timerStatus) {
                    TimerStatus.PAUSED -> ++remainingTimeSeconds

                    TimerStatus.STOPPED -> state.onTimerFinished()

//                  PLAYING & RESUMED
                    else -> {
                        if (remainingTimeSeconds == 0) remainingTimeSeconds = state.totalTimeSeconds

                        for (second in remainingTimeSeconds downTo 1) {
                            emit(second)

                            remainingTimeSeconds--

                            delay(1000)
                        }

                        state.onTimerFinished()
                    }
                }
            }.flowOn(defaultDispatchers)
        }
    }
}