package view.botservices.schedule

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable

@Serializable
data class CommonRequestParam(val name: String = "", val date: String = "")

class ScheduleApiService {
    private val client: HttpClient = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(format)
        }
    }

    suspend fun getSchedule(name: String, date: String): Schedule {
        val schedule: Schedule
        runBlocking {
            schedule = client.get("http://localhost:9090/api/schedule") {
                contentType(ContentType.Application.Json)
                body = CommonRequestParam(name, date)
            }
        }
        return schedule
    }

    suspend fun getCurrentSchedule(name: String): Schedule {
        val schedule: Schedule
        runBlocking {
            schedule = client.get("http://localhost:9090/api/schedule/current") {
                contentType(ContentType.Application.Json)
                body = CommonRequestParam(name)
            }
        }
        return schedule
    }

    //TODO : add more functions (getSchedules() and getNextLesson())

}