package alfredabdo.android.games.idlegame.data.session

import alfredabdo.android.games.idlegame.data.GameItemSavedState
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.firstOrNull

class SessionDataStore(
    val dataStore: DataStore<Session>,
) {
    suspend fun get(): Session? = dataStore.data.firstOrNull()

    suspend fun save(session: Session): Session = dataStore.updateData { session }

    suspend fun save(
        coins: Double,
        gameItemStates: Map<String, GameItemSavedState>,
        activeAchievementId: String?,
    ): Session = dataStore.updateData {
        it.copy(
            coins = coins,
            gameItemStates = gameItemStates,
            activeAchievementId = activeAchievementId,
        )
    }

    suspend fun saveUser(
        userId: UInt?,
        username: String?,
    ): Session = dataStore.updateData {
        it.copy(
            userId = userId,
            username = username,
        )
    }

    suspend fun clearUser() = saveUser(null, null)

    suspend fun clear() = dataStore.updateData { Session.default }
}