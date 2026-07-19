package alfredabdo.android.games.idlegame.base.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseMVIViewModel<State : Any, Action : Any, Event : Any> : BaseViewModel() {

    abstract val initialState: State


    private val _uiState: MutableStateFlow<State> = MutableStateFlow(initialState)
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    private val _actions: MutableSharedFlow<Action> = MutableSharedFlow()
    val actions: SharedFlow<Action> = _actions.asSharedFlow()

    private val _events: Channel<Event> = Channel()
    val events: Flow<Event> = _events.receiveAsFlow()

    private val _tempEvents: MutableSharedFlow<Event> = MutableSharedFlow(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val tempEvents: SharedFlow<Event> = _tempEvents.asSharedFlow()


    init {
        viewModelScope.launch { onInit() }
    }

    protected open suspend fun onInit() {
        actions.collect { handleAction(it) }
    }

    protected abstract suspend fun handleAction(action: Action)


    fun runAction(action: Action) = viewModelScope.launch {
        _actions.emit(action)
    }

    protected fun sendEvent(event: Event) = viewModelScope.launch {
        _events.send(event)
    }

    protected fun sendTempEvent(event: Event) = viewModelScope.launch {
        _tempEvents.emit(event)
    }

    protected open fun updateUIState(provider: (State) -> State) {
        _uiState.update(provider)
    }
}