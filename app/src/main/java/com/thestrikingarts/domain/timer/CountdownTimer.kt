package com.thestrikingarts.domain.timer

import com.thestrikingarts.di.DefaultDispatcher
import com.thestrikingarts.di.MainDispatcher
import com.thestrikingarts.domain.model.TimerState
import com.thestrikingarts.domain.model.TimerStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject


class CountdownTimer @Inject constructor(
    @DefaultDispatcher private val defaultDispatchers: CoroutineDispatcher,
    @MainDispatcher private val mainDispatchers: CoroutineDispatcher
) {
    private var remainingTimeSeconds = 0

    @OptIn(ExperimentalCoroutinesApi::class)
    val timerFlow: (timerState: StateFlow<TimerState>) -> Flow<Int> = { timerState ->
        timerState.flatMapLatest { state ->
            flow {
                when (state.timerStatus) {
                    TimerStatus.PAUSED -> ++remainingTimeSeconds

                    TimerStatus.STOPPED -> withContext(mainDispatchers) { state.onTimerFinished() }

//                  PLAYING & RESUMED
                    else -> {
                        if (remainingTimeSeconds == 0) remainingTimeSeconds = state.totalTimeSeconds

                        for (second in remainingTimeSeconds downTo 1) {
                            emit(second)

                            remainingTimeSeconds--

                            delay(1000)
                        }

                        withContext(mainDispatchers) { state.onTimerFinished() }
                    }
                }
            }.flowOn(defaultDispatchers)
        }
    }
}