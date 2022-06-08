package view.botservices

import net.dv8tion.jda.api.EmbedBuilder
import view.botservices.database.DatabaseOperations
import view.botservices.database.DiscordDBO
import view.platforms.discord.logic.services.commandmanagment.CommandContext

interface ISetting {
    val dbo: DatabaseOperations

    fun name(): String

    fun info(): String
}

interface IDiscordSetting : ISetting {
    override val dbo: DatabaseOperations
        get() = DiscordDBO()

    fun aliases(): List<String>

    fun get(ctx: CommandContext): EmbedBuilder

    fun set(ctx: CommandContext): EmbedBuilder

    fun delete(ctx: CommandContext): EmbedBuilder

    fun showError(): EmbedBuilder
}