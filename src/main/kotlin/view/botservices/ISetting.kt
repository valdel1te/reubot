package view.botservices

import net.dv8tion.jda.api.EmbedBuilder
import view.platforms.discord.logic.services.commandmanagment.CommandContext

interface ISetting {
    val dbo: DatabaseOperations
        get() = DatabaseOperations()

    fun name(): String

    fun info(): String
}

interface IDiscordSetting : ISetting {
    fun aliases(): List<String>

    fun get(ctx: CommandContext): EmbedBuilder

    fun set(ctx: CommandContext): EmbedBuilder

    fun delete(ctx: CommandContext): EmbedBuilder

    fun showError(): EmbedBuilder
}