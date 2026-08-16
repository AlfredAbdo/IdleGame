package alfredabdo.android.games.idlegame.features.game

import alfredabdo.android.games.idlegame.base.viewmodel.BaseViewModel
import alfredabdo.android.games.idlegame.data.GameAchievement
import alfredabdo.android.games.idlegame.data.exception.AppException
import alfredabdo.android.games.idlegame.features.game.data.GetGameStateUseCase
import alfredabdo.android.games.idlegame.features.game.data.GetNextAchievementUseCase
import alfredabdo.android.games.idlegame.features.game.data.HomeGameState
import alfredabdo.android.games.idlegame.features.game.data.SaveGameStateUseCase
import alfredabdo.android.games.idlegame.ui.state.UIState
import alfredabdo.android.games.idlegame.util.game.GameCalculator
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds

class GameViewModel(
    private val calculator: GameCalculator,
    private val getGameStateUseCase: GetGameStateUseCase,
    private val saveGameStateUseCase: SaveGameStateUseCase,
    private val getNextAchievementUseCase: GetNextAchievementUseCase,
) : BaseViewModel() {

    private val _state = MutableStateFlow(HomeGameState())
    val state get() = _state.asStateFlow()

    private val _loadingState = MutableStateFlow<UIState<String>?>(null)
    val loadingState get() = _loadingState.asStateFlow()

    private val _showAchievementState: MutableStateFlow<GameAchievement?> = MutableStateFlow(null)
    val showAchievementState get() = _showAchievementState.asStateFlow()


    private var currentFrameNanos: Long? = null


    init {
        viewModelScope.launch {
            _state.update { getGameStateUseCase(Unit) }
        }
    }


    fun start(frame: Long) {
        currentFrameNanos = frame
    }

    //fixme turn this into a single job that runs in Dispatchers.Default
    //fixme try to refactor the model so we can run the states updates in parallel with an atomic coins update
    fun update(frame: Long) {
        val diff = (frame - (currentFrameNanos ?: 0L)).nanoseconds
        val gameState = _state.value
        var coins = gameState.coins
        val newGameState = gameState.copy(
            states = gameState.states.onEach { (_, value) ->
                if (value.unlocked) {
                    var progress = value.progress + (diff / value.fillRate)
                    if (progress >= 1.0) {
                        progress = 0.0
                        coins += value.gain // gain coins!
                    }
                    value.progress = progress
                }
            },
            coins = coins,
        )
        currentFrameNanos = frame
        _state.update { newGameState }
    }

    fun unlock(id: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                val gameState = _state.value.copy()
                val coins = gameState.coins
                val newGameState = gameState.copy(
                    states = gameState.states.apply {
                        entries.find { it.key.id == id }?.let { (key, value) ->
                            if (key.unlockAmount == null || coins >= key.unlockAmount) {
                                value.unlocked = true
                            }
                        }
                    },
                    coins = coins,
                )
                _state.update { newGameState }
            }
        }
    }

    fun upgrade(id: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                val gameState = _state.value.copy()
                var coins = gameState.coins
                val newGameState = gameState.copy(
                    states = gameState.states.apply {
                        entries.find { it.key.id == id }?.let { (key, value) ->
                            if (coins >= value.upgradeCost) {
                                val multipliers = key.upgradeMultipliers

                                coins -= value.upgradeCost
                                val nextLevel = value.level + 1
                                value.gain = calculator.calculateGain(
                                    key.baseGain,
                                    multipliers.gainMultiplier,
                                    nextLevel,
                                )
                                value.fillRate = calculator.calculateFillRate(
                                    key.baseFillRate,
                                    multipliers.fillRateMultiplier,
                                    nextLevel,
                                )
                                value.upgradeCost = calculator.calculateCost(
                                    key.baseUpgradeCost,
                                    multipliers.costMultiplier,
                                    nextLevel,
                                )
                                value.level = nextLevel
                            }
                        }
                    },
                    coins = coins,
                )
                _state.update { newGameState }
            }
        }
    }

    fun save() {
        viewModelScope.launch {
            useCaseFlow(saveGameStateUseCase, _state.value)
                .handleResult(
                    onLoading = {
                        _loadingState.value = UIState.Loading(additionalData)
                    },
                    onError = { exception ->
                        val wrappedException = AppException("Something went wrong while saving your game.", exception)
                        _loadingState.value = UIState.Error(wrappedException)
                    },
                    onSuccess = {
                        _loadingState.value = UIState.Success("Your progress was saved!")
                    },
                )
        }
    }

    //fixme turn this into a single job that runs in Dispatchers.Default; for now, only create a job (launch) if needed
    fun checkActiveAchievement() {
        val gameState = _state.value
        val currentAchievement = gameState.activeAchievement

        if (currentAchievement?.condition?.invoke(gameState.coins, gameState.states) == true) {
            viewModelScope.launch {
                withContext(Dispatchers.Default) {
                    completeActiveAchievement()
                }
            }
        }
    }


    private suspend fun completeActiveAchievement() {
        val currentAchievement = _state.value.activeAchievement

        _showAchievementState.value = currentAchievement
        delay(3.seconds) //show achievement done
        _showAchievementState.value = null

        val achievement = getNextAchievementUseCase(currentAchievement?.id)
        val newGameState = _state.value.copy(activeAchievement = achievement)
        _state.update { newGameState }
    }
}