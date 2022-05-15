package view.platforms.discord.logic.commands.settings

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.TextChannel
import view.platforms.discord.logic.services.commandmanagment.CommandContext
import view.platforms.discord.logic.services.commandmanagment.ICommand
import view.platforms.discord.logic.services.embeds.EmbedUserSettings

class SetSettings : ICommand {
    override fun name(): String =
        "settings"

    override fun handle(ctx: CommandContext) {
        val args = ctx.args
        val textChannel = ctx.event.textChannel
        val author = ctx.event.author.asTag

        if (args.isEmpty()) {
            getSettingsList(textChannel, author)
            return
        }

        for ((name, setting) in SETTINGS) {
            val namesAreEquals =
                args[0] == name
            val aliasWithNameAreEquals =
                setting.aliases().find { alias -> args[0] == alias } != null

            if (namesAreEquals || aliasWithNameAreEquals) {
                val embed: EmbedBuilder = if (args.size == 2)
                    setting.set(ctx)
                else
                    setting.get(ctx)
                textChannel.sendMessageEmbeds(embed.build()).queue()
                return
            }
        }

        getSettingsList(textChannel, author)
    }

    private fun getSettingsList(tc: TextChannel, authorMessage: String) =
        tc.sendMessageEmbeds(EmbedUserSettings(authorMessage).configInfo(SETTINGS).build()).queue()
}