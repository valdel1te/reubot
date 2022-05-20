package view.platforms.discord.logic.commands.settings

import view.botservices.database.DiscordDBO
import view.platforms.discord.logic.services.commandmanagment.ICommand
import view.platforms.discord.logic.services.commandmanagment.CommandContext
import view.platforms.discord.logic.services.embeds.EmbedUserSettings

class GetConfig : ICommand {
    override fun name(): String =
        "config"

    override fun handle(ctx: CommandContext) {
        val dbo = DiscordDBO()
        val configList = dbo.getClientProperties(ctx.event.guild.idLong)

        val stringBuilder = StringBuilder()
        for ((key, value) in configList) {
            stringBuilder.append("`$key`: $value\n".replace("none", "Отсутствует"))
        }

        ctx.event.channel.sendMessageEmbeds(
            EmbedUserSettings(ctx.event.author.asTag).userAllSettings(
                stringBuilder.toString(),
                ctx.event.guild.name
            ).build()
        ).queue()
    }
}