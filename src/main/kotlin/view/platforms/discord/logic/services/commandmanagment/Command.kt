package view.platforms.discord.logic.services.commandmanagment

import net.dv8tion.jda.api.EmbedBuilder
import view.botservices.CommandInfoReader
import view.platforms.discord.logic.services.embeds.EmbedHelp

interface Command {
    fun handle(ctx: CommandContext) =
        ctx.event.channel.sendMessage("что то пошло не так....").queue() // todo : Error embed

    fun getName(): String =
        "unknowncommand"

    fun getHelp(): EmbedBuilder =
        EmbedHelp.commandInfo(CommandInfoReader().getDiscordCommand(getName()))

    fun needAdministratorPermission(): Boolean =
        CommandInfoReader().getDiscordCommand(getName()).needAdminRole

    fun getAliases(): List<String> =
        CommandInfoReader().getDiscordCommand(getName()).aliases
}