package view.platforms.discord.logic.services.commandmanagment

import net.dv8tion.jda.api.EmbedBuilder
import view.botservices.CommandInfoReader
import view.platforms.discord.logic.services.embeds.EmbedHelp

interface ICommand {
    fun name(): String =
        "unknowncommand"

    fun handle(ctx: CommandContext) =
        ctx.event.channel.sendMessage("что то пошло не так....").queue() // todo : Error embed

    fun help(): EmbedBuilder =
        EmbedHelp.commandInfo(CommandInfoReader().getDiscordCommand(name()))

    fun needAdministratorPermission(): Boolean =
        CommandInfoReader().getDiscordCommand(name()).needAdminRole

    fun aliases(): List<String> =
        CommandInfoReader().getDiscordCommand(name()).aliases
}