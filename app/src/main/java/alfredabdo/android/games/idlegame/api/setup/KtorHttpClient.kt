package alfredabdo.android.games.idlegame.api.setup

import alfredabdo.android.games.idlegame.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.ConnectionSpec
import kotlin.time.Duration.Companion.seconds

fun ktorHTTPClient(
    json: Json,
): HttpClient = HttpClient(OkHttp) {
    engine {
        config {
            connectionSpecs(
                listOf(
                    ConnectionSpec.MODERN_TLS,
//                    ConnectionSpec.COMPATIBLE_TLS,
                    ConnectionSpec.CLEARTEXT,
                )
            )
        }
    }

    install(ContentNegotiation) {
        json(json)
    }

    install(HttpTimeout) {
        requestTimeoutMillis = 20.seconds.inWholeMilliseconds
    }

    install(Logging) {
        logger = Logger.ANDROID
        level = LogLevel.ALL
        //...
    }

    //...

    defaultRequest {
        url(BuildConfig.API_BASE_URL)
        contentType(ContentType.Application.Json)
    }
}