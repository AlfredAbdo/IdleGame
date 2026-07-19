package alfredabdo.android.games.idlegame.data.session

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.firstOrNull

class SessionDataStore(
    val dataStore: DataStore<Session>,
) {
    suspend fun get(): Session? = dataStore.data.firstOrNull()

    suspend fun save(session: Session): Session = dataStore.updateData { session }

    suspend fun clear() = dataStore.updateData { Session.default }
}