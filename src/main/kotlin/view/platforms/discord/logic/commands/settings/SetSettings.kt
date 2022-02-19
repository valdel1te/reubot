package view.platforms.discord.logic.commands.settings

import net.dv8tion.jda.api.entities.TextChannel
import view.platforms.discord.logic.commands.services.Command
import view.platforms.discord.logic.commands.services.CommandContext

class SetSettings : Command {
    override fun handle(ctx: CommandContext) {
        if (ctx.authorIsNotAdmin())
            return

        if (ctx.args.isEmpty()) {
            getSettingsList()
            return
        }

        when (ctx.args[0]) {
            "prefix" -> {
                if (ctx.args.size == 2)
                    setPrefix()
                else
                    getPrefix(ctx.event.textChannel)
            }

            "subchannel" -> {
                if (ctx.args.size == 2)
                    setSubChannel()
                else
                    getSubChannel()
            }

            //"group" -> // TODO group setting

            else -> getSettingsList()
        }

    }

    override fun getName(): String =
        "settings"

    override fun getHelp(): String =
        "настроечки)\nпотом распишу"

    override fun needAdministratorPermission(): Boolean =
        true

    private fun getSettingsList() {
        // TODO оформить эмбед со списком команд
    }

    private fun getPrefix(textChannel: TextChannel) {


        textChannel.sendMessage("шлюха китайская ты слсышишь").queue()
    }

    private fun setPrefix() {

    }

    private fun getSubChannel() {

    }

    private fun setSubChannel() {

    }
}