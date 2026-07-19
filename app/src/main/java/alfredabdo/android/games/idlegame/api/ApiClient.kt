package alfredabdo.android.games.idlegame.api

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.setBody
import kotlinx.serialization.json.JsonArrayBuilder
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject

class ApiClient(
    private val client: HttpClient,
) {

    private inline fun HttpRequestBuilder.jsonBody(block: JsonObjectBuilder.() -> Unit) {
        setBody(buildJsonObject(block))
    }

    private inline fun HttpRequestBuilder.jsonListBody(block: JsonArrayBuilder.() -> Unit) {
        setBody(buildJsonArray(block))
    }


    /*suspend fun login(
        username: String,
        password: String,
    ): UserResponseDTO = client.post("login") {
        jsonBody {
            put("username", username)
            put("password", password)
        }
    }.body()*/

    //...
}