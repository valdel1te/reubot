package view.botservices

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.io.File

const val PATH = "src/main/resources/commandinfo.json"

class CommandInfoReader {
    private val jsonFile: JsonElement = Json.parseToJsonElement(File(PATH).readText(Charsets.UTF_8))
    private val platforms: PlatformsHelp = Json.decodeFromString(jsonFile.toString())

    fun getDiscordCommand(name: String): DiscordCommand =
        platforms.discord.first { command -> command.name == name }

    fun getAllDiscordCommands(): List<DiscordCommand> =
        platforms.discord.sortedBy { command -> command.category }
}

@Serializable
data class PlatformsHelp(
    val discord: List<DiscordCommand>,
    val telegram: List<TelegramCommand>,
    val vkontakte: List<VkCommand>
)

@Serializable
data class DiscordCommand(
    val commandName: String,
    val category: String,
    val name: String,
    val help: String,
    val needAdminRole: Boolean,
    val example: String,
    val aliases: List<String>
)

@Serializable
data class TelegramCommand(val s: String)

@Serializable
data class VkCommand(val s: String)
