package alfredabdo.android.games.idlegame.api

import alfredabdo.android.games.idlegame.data.api.GameItemStateDTO
import alfredabdo.android.games.idlegame.data.api.MessageResponseDTO
import alfredabdo.android.games.idlegame.data.api.UserResponseDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArrayBuilder
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.put

class ApiClient(
    private val client: HttpClient,
    private val json: Json,
) {

    private inline fun HttpRequestBuilder.jsonBody(block: JsonObjectBuilder.() -> Unit) {
        setBody(buildJsonObject(block))
    }

    private inline fun HttpRequestBuilder.jsonListBody(block: JsonArrayBuilder.() -> Unit) {
        setBody(buildJsonArray(block))
    }


    suspend fun getUser(userId: UInt): UserResponseDTO = client.get("user") {
        header("userId", userId)
    }.body()

    suspend fun getOrCreateUser(username: String): UserResponseDTO = client.post("user") {
        jsonBody {
            put("username", username)
        }
    }.body()

    suspend fun save(
        userId: UInt,
        coins: Double,
        states: Map<UInt, GameItemStateDTO>,
        activeAchievementId: UInt?,
    ): MessageResponseDTO = client.post("save") {
        header("userId", userId)
        jsonBody {
            put("coins", coins)
            put("states", json.encodeToJsonElement(states))
            put("activeAchievementId", activeAchievementId?.toLong())
        }
    }.body()

    suspend fun deleteSave(userId: UInt): MessageResponseDTO = client.delete("save") {
        header("userId", userId)
    }.body()
}