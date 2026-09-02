package alfredabdo.android.games.idlegame.data.repo

import alfredabdo.android.games.idlegame.BuildConfig
import alfredabdo.android.games.idlegame.api.ApiClient
import alfredabdo.android.games.idlegame.data.GameAchievement
import alfredabdo.android.games.idlegame.data.GameItem
import alfredabdo.android.games.idlegame.data.GameItemSavedState
import alfredabdo.android.games.idlegame.data.GameSavedState
import alfredabdo.android.games.idlegame.data.api.GameItemStateDTO
import alfredabdo.android.games.idlegame.data.session.Session
import alfredabdo.android.games.idlegame.data.session.SessionDataStore
import alfredabdo.android.games.idlegame.ui.animation.gameInfiniteAnimationThreshold
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class GameRepository(
    private val sessionDataStore: SessionDataStore,
    private val apiClient: ApiClient,
) {

    companion object {
        private const val useServer = BuildConfig.SHOULD_CONNECT_TO_SERVER
    }


    suspend fun hasLocalSave(): Boolean = sessionDataStore.get().let { it != null && it.userId == null }

    // useServer should be true coming here
    suspend fun getOrCreateSessionFor(username: String): String = if (useServer) {
        val response = apiClient.getOrCreateUser(username)
        val user = response.user
        gameItems = response.gameItems?.map(GameItem::fromDTO)
        achievements = response.achievements?.map(GameAchievement::fromDTO)

        sessionDataStore.save(
            Session(
                user?.coins ?: 0.0,
                user?.states
                    ?.mapValues { (key, value) ->
                        val item = gameItemsOrDefault.first { it.id == key }

                        GameItemSavedState(
                            key,
                            value.level,
                            value.unlocked == true,
                            value.fillRateMs?.milliseconds ?: item.baseFillRate,
                            value.gain ?: item.baseGain,
                            value.upgradeCost ?: item.baseUpgradeCost,
                            value.progress ?: 0.0,
                        )
                    }
                    ?: gameItemsOrDefault.associateBy(
                        keySelector = { item -> item.id },
                        valueTransform = { item ->
                            GameItemSavedState(
                                item.id,
                                1,
                                item.unlockAmount == null,
                                item.baseFillRate,
                                item.baseGain,
                                item.baseUpgradeCost,
                            )
                        },
                    ),
                user?.activeAchievement?.id?.toString(),
                user?.userId,
                user?.username,
            )
        )

        return user?.username.orEmpty()
    } else {
        ""
    }

    suspend fun getOrCreateSessionWithLocalSaveFor(username: String): String = if (useServer) {
        val response = apiClient.getOrCreateUser(username)
        val user = response.user
        gameItems = response.gameItems?.map(GameItem::fromDTO)
        achievements = response.achievements?.map(GameAchievement::fromDTO)

        val session = sessionDataStore.saveUser(
            user?.userId,
            user?.username,
        )

        uploadSaveToServer(session)

        return user?.username.orEmpty()
    } else {
        ""
    }

    suspend fun getItems(): List<GameItem> = gameItemsOrDefault

    suspend fun getGameState(): GameSavedState {
        val gameItems = getItems()

        return sessionDataStore.get()?.let { session ->
            val itemsMap = buildMap {
                gameItems.forEach { gameItem ->
                    this[gameItem] = session.gameItemStates[gameItem.id]
                        ?: GameItemSavedState(
                            gameItem.id,
                            1,
                            gameItem.unlockAmount == null,
                            gameItem.baseFillRate,
                            gameItem.baseGain,
                            gameItem.baseUpgradeCost,
                        )
                }
            }.toMutableMap()

            val activeAchievement = session.activeAchievementId
                ?.let { activeId ->
                    if (activeId == Session.USE_FIRST_ACHIEVEMENT_ID) {
                        achievementsOrDefault.firstOrNull()
                    } else {
                        achievementsOrDefault.find { it.id == activeId }
                    }
                }

            GameSavedState(
                session.coins,
                itemsMap,
                activeAchievement,
            )
        } ?: GameSavedState(
            0.0,
            gameItems.associateWith { item ->
                GameItemSavedState(
                    item.id,
                    1,
                    item.unlockAmount == null,
                    item.baseFillRate,
                    item.baseGain,
                    item.baseUpgradeCost,
                )
            },
            achievementsOrDefault.firstOrNull(),
        )
    }

    @Suppress("SimplifyBooleanWithConstants")
    suspend fun saveGameState(state: GameSavedState) {
        val session = sessionDataStore.save(
            state.coins,
            state.states.mapKeys { (key, _) -> key.id },
            state.activeAchievement?.id,
        )
        if (useServer && session.userId != null) {
            uploadSaveToServer(session)
        }
    }

    suspend fun getNextAchievement(currentAchievementId: String?): GameAchievement? {
        if (currentAchievementId == null) {
            return null
        }

        val achievements = achievementsOrDefault
        val index = achievements.indexOfFirst { it.id == currentAchievementId }.takeUnless { it == -1 } ?: return null
        return achievements.getOrNull(index + 1)
    }

    suspend fun clearGameState(): Boolean {
        return try {
            if (useServer) {
                val userId = sessionDataStore.get()?.userId
                if (userId != null) {
                    apiClient.deleteSave(userId)
                }
            }
            sessionDataStore.clear()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    private suspend fun uploadSaveToServer(session: Session) {
        apiClient.save(
            requireNotNull(session.userId) { "Missing userId!" },
            session.coins,
            session.gameItemStates
                .entries
                .associateBy(
                    keySelector = { (key, _) -> key.toUInt() },
                    valueTransform = { (key, value) ->
                        GameItemStateDTO(
                            value.level,
                            value.unlocked,
                            value.fillRate.inWholeMilliseconds,
                            value.gain,
                            value.upgradeCost,
                            value.progress,
                        )
                    },
                ),
            session.activeAchievementId?.toUIntOrNull(),
        )
    }


    private val _gameItems by lazy {
        listOf(
            GameItem(
                "1",
                "Pocket change",
                "Rely on your parents, ...",
                2.seconds,
                10.0,
                GameItem.UpgradeMultipliers(1.5, 1.2, 1.3),
                0.0,
                20.0,
            ),
            GameItem(
                "2",
                "Work as an employee",
                "Become a developer in a company.",
                5.seconds,
                90.0,
                GameItem.UpgradeMultipliers(1.5, 1.2, 1.3),
                100.0,
                200.0,
            ),
            GameItem(
                "3",
                "Freelancer",
                "Work as a developer freelancer, with no one above you :).",
                30.seconds,
                500.0,
                GameItem.UpgradeMultipliers(1.5, 1.2, 1.3),
                500.0,
                700.0,
            ),
            GameItem(
                "4",
                "Create a company",
                "Create your own company, and give orders to other developers.",
                2.minutes,
                2_000.0,
                GameItem.UpgradeMultipliers(1.5, 1.2, 1.3),
                3_000.0,
                4_000.0,
            ),
            GameItem(
                "5",
                "Invest in real estate",
                "Buy some lands and make easy money.",
                10.minutes,
                10_000.0,
                GameItem.UpgradeMultipliers(1.5, 1.2, 1.3),
                20_000.0,
                40_000.0,
            ),
            GameItem(
                "6",
                "Crypto mining",
                "Buy crypto-mining machines and harness the power of the crypto-currency; hopefully they will not lose their value :(.",
                30.minutes,
                30_000.0,
                GameItem.UpgradeMultipliers(1.5, 1.2, 1.3),
                60_000.0,
                120_000.0,
            ),
            GameItem(
                "7",
                "Sell trading cards",
                "Spend your time buying and selling all the trading cards for maximum profit! You don't care about the games using them, just their value.",
                1.hours,
                120_000.0,
                GameItem.UpgradeMultipliers(1.8, 1.3, 1.5),
                240_000.0,
                480_000.0,
            ),
        )
    }

    private val _achievements by lazy {
        listOf(
            GameAchievement(
                "1",
                "Purchase your first source of income!",
                "3, 2, 1, Go!",
            ) { _, itemStates ->
                itemStates.entries.firstOrNull()
                    ?.value
                    ?.unlocked ?: false
            },
            GameAchievement(
                "2",
                "Get at least 1,000 coins",
                "Your first thousand.",
            ) { coins, _ ->
                coins >= 1_000
            },
            GameAchievement(
                "3",
                "Get any item duration to $gameInfiniteAnimationThreshold or below",
                "NOT THE EYES!",
            ) { _, itemStates ->
                itemStates.entries.any {
                    it.value.fillRate <= gameInfiniteAnimationThreshold
                }
            },
            GameAchievement(
                "4",
                "Get at least 1,000,000 coins",
                "You're a millionaire, Harry!",
            ) { coins, _ ->
                coins >= 1_000_000
            },
            GameAchievement(
                "5",
                "Get at least 1B coins",
                "When you have nothing else to do (thank you for playing the game).",
            ) { coins, _ ->
                coins >= 1_000_000_000
            },
        )
    }

    private var gameItems: List<GameItem>? = null
    private var achievements: List<GameAchievement>? = null

    private val gameItemsOrDefault get() = gameItems ?: _gameItems
    private val achievementsOrDefault get() = achievements ?: _achievements
}