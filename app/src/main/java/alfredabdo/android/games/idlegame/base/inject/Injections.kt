package alfredabdo.android.games.idlegame.base.inject

import alfredabdo.android.games.idlegame.App
import alfredabdo.android.games.idlegame.api.ApiClient
import alfredabdo.android.games.idlegame.api.setup.ktorHTTPClient
import alfredabdo.android.games.idlegame.data.exception.AppExceptionConverter
import alfredabdo.android.games.idlegame.data.repo.GameRepository
import alfredabdo.android.games.idlegame.data.session.Session
import alfredabdo.android.games.idlegame.data.session.SessionDataStore
import android.app.Application
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.dataStoreFile
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object Injections {

    private lateinit var _app: App

    fun init(app: App) {
        this._app = app
    }


    val app: App get() = _app
    val application: Application get() = _app

    val dataJson: Json by lazy {
        Json {
            explicitNulls = false
            ignoreUnknownKeys = true
            coerceInputValues = true
            useAlternativeNames = false
        }
    }

    val sessionDataStore: SessionDataStore by lazy {
        SessionDataStore(
            DataStoreFactory.create(
                serializer = object : Serializer<Session> {
                    override val defaultValue: Session = Session.default

                    override suspend fun readFrom(input: InputStream): Session =
                        try {
                            dataJson.decodeFromString<Session>(
                                input.readBytes().decodeToString()
                            )
                        } catch (serialization: SerializationException) {
                            throw CorruptionException("Unable to read Session", serialization)
                        }

                    override suspend fun writeTo(t: Session, output: OutputStream) {
                        output.write(
                            dataJson.encodeToString(t)
                                .encodeToByteArray()
                        )
                    }
                },
                produceFile = {
                    _app.dataStoreFile("session.json")
                },
            )
        )
    }

    val apiJson: Json by lazy {
        Json {
            explicitNulls = false
            ignoreUnknownKeys = true
        }
    }

    val apiClient: ApiClient by lazy {
        ApiClient(ktorHTTPClient(apiJson))
    }

    val exceptionConverter: AppExceptionConverter by lazy {
        AppExceptionConverter(_app, apiJson)
    }

    val gameRepo: GameRepository by lazy {
        GameRepository(sessionDataStore)
    }

    //...
}