package ru.kartollika.yandexcup.mvi2

import android.util.Log
import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

abstract class MVIFeature<State : MVIState, Action : MVIAction, Event : MVIEvent>(
  private val exceptionHandler: CoroutineExceptionHandler = defaultExceptionHandler,
) : ActionConsumer<Action>, StateOwner<State>, EventsOwner<Event> {
  protected abstract fun initialState(): State
  protected abstract suspend fun processAction(
    state: State,
    action: Action,
  )

  protected abstract fun reduce(
    state: State,
    action: Action,
  ): State

  lateinit var coroutineScope: CoroutineScope

  // UI state
  private val _uiState: MutableStateFlow<State> by lazy { MutableStateFlow(initialState()) }
  override val state: StateFlow<State>
    get() = _uiState.asStateFlow()

  // UI one time events
  private val _uiEvents: MutableSharedFlow<Event> by lazy {
    MutableSharedFlow(extraBufferCapacity = 1)
  }
  override val events: SharedFlow<Event>
    get() = _uiEvents.asSharedFlow()

  val _actions: MutableSharedFlow<Action> by lazy {
    MutableSharedFlow(extraBufferCapacity = 2)
  }

  protected var savedState: SavedState? = null

  @VisibleForTesting
  val actions: SharedFlow<Action>
    get() = _actions.asSharedFlow()

  fun startFeature() {
    val scope = coroutineScope + exceptionHandler
    scope.launch {
      _actions
        .collect { action ->
          val currentState = _uiState.value

          scope.launch {
            processAction(state = currentState, action = action)
          }

          _uiState.value = reduce(state = currentState, action = action)
        }
    }
  }

  fun initSavedState(savedState: SavedState?) {
    this.savedState = savedState
  }

  override fun consumeAction(action: Action) {
    _actions.tryEmit(action)
  }

  protected fun consumeEvent(event: Event) {
    _uiEvents.tryEmit(event)
  }
}

val defaultExceptionHandler by lazy {
  CoroutineExceptionHandler { _, throwable ->
    Log.e(
      "MVIFeature", "Uncaught exception: $throwable", throwable
    )
  }
}