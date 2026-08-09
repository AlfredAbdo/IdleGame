package alfredabdo.android.games.idlegame.features.login

import alfredabdo.android.games.idlegame.base.viewmodel.BaseMVIViewModel
import alfredabdo.android.games.idlegame.data.exception.AppException
import alfredabdo.android.games.idlegame.data.repo.GameRepository
import alfredabdo.android.games.idlegame.data.session.SessionDataStore
import alfredabdo.android.games.idlegame.features.login.data.LoginUseCase
import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginViewModel(
    private val sessionDataStore: SessionDataStore,
    private val gameRepo: GameRepository,
    private val loginUseCase: LoginUseCase,
) : BaseMVIViewModel<LoginViewModel.State, LoginViewModel.Action, LoginViewModel.Event>() {

    override val initialState: State
        get() = State.Fetching


    @Immutable
    sealed interface State {
        @Immutable
        data object Fetching : State

        @Immutable
        data class Fetched(
            val isLoggedIn: Boolean,
            val loggedInUsername: String?,
            val hasLocalSave: Boolean,
            val showLoading: Boolean = false,
        ) : State
    }

    sealed interface Action {
        object FetchSaveInfo : Action

        object UseLocalSave : Action

        class Login(
            val username: String,
            val shouldCarryLocalSaveOver: Boolean,
        ) : Action
    }

    sealed interface Event {

        class Error(val exception: AppException) : Event

        object RedirectToHome : Event
    }


    override suspend fun handleAction(action: Action) {
        when (action) {
            Action.FetchSaveInfo -> fetchSaveInfo()
            Action.UseLocalSave -> useLocalSave()
            is Action.Login -> login(action)
        }
    }


    private fun fetchSaveInfo() {
        viewModelScope.launch {
            updateUIState { State.Fetching }
            val session = sessionDataStore.get()
            val isLoggedIn = session?.userId != null
            val loggedInUsername = session?.username
            val hasLocalSave = gameRepo.hasLocalSave()
            updateUIState { State.Fetched(isLoggedIn, loggedInUsername, hasLocalSave) }
        }
    }

    private fun useLocalSave() {
        sendEvent(Event.RedirectToHome)
    }

    private fun login(params: Action.Login) {
        viewModelScope.launch {
            useCaseFlow(loginUseCase, LoginUseCase.Params(params.username, params.shouldCarryLocalSaveOver))
                .handleResult(
                    onLoading = {
                        updateUIState { state ->
                            (state as? State.Fetched)?.copy(
                                showLoading = true,
                            ) ?: state
                        }
                    },
                    onError = {
                        updateUIState { state ->
                            (state as? State.Fetched)?.copy(
                                showLoading = false,
                            ) ?: state
                        }

                        sendTempEvent(Event.Error(it))
                    },
                    onSuccess = {
                        updateUIState { state ->
                            (state as? State.Fetched)?.copy(
                                showLoading = false,
                            ) ?: state
                        }

                        sendEvent(Event.RedirectToHome)
                    },
                )
        }
    }
}