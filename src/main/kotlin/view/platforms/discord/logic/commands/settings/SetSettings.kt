package view.platforms.discord.logic.commands.settings

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.TextChannel
import view.botservices.DatabaseOperations
import view.platforms.discord.logic.commands.services.Command
import view.platforms.discord.logic.commands.services.CommandContext
import java.awt.Color

class SetSettings : Command {
    override fun handle(ctx: CommandContext) {
        val dbo = DatabaseOperations(ctx.sessionFactory)

        if (ctx.authorIsNotAdmin())
            return

        if (ctx.args.isEmpty()) {
            getSettingsList()
            return
        }

        when (ctx.args[0]) {
            "prefix" -> {
                if (ctx.args.size == 2)
                    setPrefix(ctx.event.textChannel, dbo, ctx.event.guild.idLong, ctx.args[1])
                else
                    getPrefix(ctx.event.textChannel, dbo, ctx.event.guild.idLong)
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

    private fun getPrefix(
        textChannel: TextChannel,
        dbo: DatabaseOperations,
        chatId: Long
    ) {
        val prefix = dbo.getDiscordPropertyValue("prefix", chatId)

        val eb = EmbedBuilder().apply {
            setColor(Color.CYAN)
            setTitle("Текущий префикс")
            setDescription(prefix)
            addField("Для смены префикса: ", "`${prefix}settings prefix [НОВЫЙ_ПРЕФИКС]`", false)
        }

        textChannel.sendMessageEmbeds(eb.build()).queue()
    }

    private fun setPrefix(
        textChannel: TextChannel,
        dbo: DatabaseOperations,
        chatId: Long,
        newPrefix: String
    ) {
        if (newPrefix.length > 3 || newPrefix.isEmpty()) {
            textChannel.sendMessage("Размер префикса должен быть не больше 3 символов").queue()
            return // TODO оформить эмбеды с ошибками и вызывать здесь специальный
        }

        dbo.updateDiscordPropertyValue("prefix", chatId, newPrefix)

        textChannel.sendMessage("Обновил").queue() // TODO здесь отображение тоже поменять
    }

    private fun getSubChannel() {

    }

    private fun setSubChannel() {

    }
}