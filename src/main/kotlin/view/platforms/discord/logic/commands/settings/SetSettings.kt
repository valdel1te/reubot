package view.platforms.discord.logic.commands.settings

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.TextChannel
import view.botservices.CommandInfoReader
import view.botservices.DatabaseOperations
import view.platforms.discord.logic.services.commandmanagment.Command
import view.platforms.discord.logic.services.commandmanagment.CommandContext
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
                if (ctx.args.size == 2) {
                    when (ctx.args[1]) {
                        "delete" -> deleteSubChannel(ctx.event.textChannel, dbo, ctx.event.guild.idLong)

                        else -> {
                            val subChannelId = ctx.args[1].replace(("[^\\d.]").toRegex(), "")

                            if (subChannelId.isEmpty() || ctx.event.guild.getGuildChannelById(subChannelId) == null )
                                return //TODO error embed

                            setSubChannel(ctx.event.textChannel, dbo, ctx.event.guild.idLong, subChannelId)
                        }
                    }
                } else
                    getSubChannel(ctx.event.textChannel, dbo, ctx.event.guild.idLong, ctx.event.guild)
            }

            //"group" -> // TODO group setting

            else -> getSettingsList()
        }

    }

    override fun getName(): String =
        "settings"


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

    private fun getSubChannel(
        textChannel: TextChannel,
        dbo: DatabaseOperations,
        chatId: Long,
        guild: Guild
    ) {
        val subChannelId = dbo.getDiscordPropertyValue("channel_id", chatId)
        val prefix = dbo.getDiscordPropertyValue("prefix", chatId)

        val eb = EmbedBuilder().apply {
            setColor(Color.CYAN)
            setTitle("Текущий канал для получения рассылки")

            if (subChannelId == "none")
                setDescription("Не указан")
            else
                setDescription(guild.getGuildChannelById(subChannelId)!!.asMention)

            addField("Для смены канала: ", "`${prefix}settings subchannel [#КАНАЛ]`", false)
            addField("Для удаления выбранного канала: ", "`${prefix}settings subchannel delete`", false)
        }

        textChannel.sendMessageEmbeds(eb.build()).queue()
    }

    private fun setSubChannel(
        textChannel: TextChannel,
        dbo: DatabaseOperations,
        chatId: Long,
        newSubChannelId: String
    ) {
        // TODO проверять айдишник на качество

        dbo.updateDiscordPropertyValue("channel_id", chatId, newSubChannelId)

        textChannel.sendMessage("Обновил").queue() // TODO здесь отображение тоже поменять
    }

    private fun deleteSubChannel(
        textChannel: TextChannel,
        dbo: DatabaseOperations,
        chatId: Long,
    ) {
        dbo.updateDiscordPropertyValue("channel_id", chatId, "none")

        textChannel.sendMessage("Обновил").queue() // TODO здесь отображение тоже поменять
    }
}