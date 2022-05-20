package view.platforms.discord.logic.commands.settings

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import view.botservices.GroupFinder
import view.botservices.IDiscordSetting
import view.platforms.discord.logic.services.commandmanagment.CommandContext
import view.platforms.discord.logic.services.embeds.*

val SETTINGS = hashMapOf<String, IDiscordSetting>(
    "prefix" to PrefixSetting(),
    "subchannel_id" to SubchannelSetting(),
    "subgroup" to SubgroupSetting()
)

private val DELETE_OPTION = listOf("delete", "del", "d")

private fun calledDeleteOption(argument: String): Boolean =
    DELETE_OPTION.find { deleteAlias -> argument == deleteAlias } != null

class PrefixSetting : IDiscordSetting {
    override fun name(): String =
        "prefix"

    override fun info(): String =
        PREFIX_INFO

    override fun aliases(): List<String> =
        listOf("p", "pr", "pfx")

    override fun get(ctx: CommandContext): EmbedBuilder {
        val title = "Текущий префикс"
        val prefix = dbo.getPropertyValue(name(), ctx.event.guild.idLong)
        val fieldHelp = MessageEmbed.Field(
            "Для смены префикса:",
            "`${prefix}settings prefix [НОВЫЙ ПРЕФИКС]`",
            false
        )

        return EmbedUserSettings(ctx.event.author.asTag).getSetting(title, prefix, listOf(fieldHelp))
    }

    override fun set(ctx: CommandContext): EmbedBuilder {
        if (calledDeleteOption(ctx.args[1]))
            return delete(ctx)

        val oldPrefix = dbo.getPropertyValue(name(), ctx.event.guild.idLong)
        val newPrefix = ctx.args[1]
        if (newPrefix.length > 3 || newPrefix.isEmpty())
            return showError()

        dbo.setPropertyValue(name(), ctx.event.guild.idLong, newPrefix)

        return EmbedUserSettings(ctx.event.author.asTag).setSetting(oldPrefix, newPrefix)
    }

    override fun delete(ctx: CommandContext): EmbedBuilder {
        val oldPrefix = dbo.getPropertyValue(name(), ctx.event.guild.idLong)

        dbo.setPropertyValue(name(), ctx.event.guild.idLong, "-")

        return EmbedUserSettings(ctx.event.author.asTag).setSetting(oldPrefix, "-")
    }

    override fun showError(): EmbedBuilder =
        EmbedError().incorrectSetSetting(REASON_ERROR_PREFIX_LENGTH)
}

class SubchannelSetting : IDiscordSetting {
    override fun name(): String =
        "subchannel_id"

    override fun info(): String =
        SUBCHANNEL_INFO

    override fun aliases(): List<String> =
        listOf("subchannel", "channel", "subch", "sbch")

    override fun get(ctx: CommandContext): EmbedBuilder {
        val title = "Текущий канал для получения рассылки"
        val subChannelId = dbo.getPropertyValue(name(), ctx.event.guild.idLong)
        val prefix = dbo.getPropertyValue("prefix", ctx.event.guild.idLong)

        val description: String = if (subChannelId == "none")
            "Не указан"
        else
            ctx.event.guild.getGuildChannelById(subChannelId)!!.asMention

        val fields = listOf<MessageEmbed.Field>(
            MessageEmbed.Field("Для смены канала: ", "`${prefix}settings subchannel [#КАНАЛ]`", false),
            MessageEmbed.Field("Для удаления выбранного канала: ", "`${prefix}settings subchannel delete`", false)
        )

        return EmbedUserSettings(ctx.event.author.asTag).getSetting(title, description, fields)
    }

    override fun set(ctx: CommandContext): EmbedBuilder {
        if (calledDeleteOption(ctx.args[1]))
            return delete(ctx)

        val oldSubChannelId = dbo.getPropertyValue(name(), ctx.event.guild.idLong)
        val newSubChannelId = ctx.args[1].replace(("[^\\d.]").toRegex(), "")
        if (ctx.event.guild.getGuildChannelById(newSubChannelId) == null || newSubChannelId.isEmpty())
            return showError()

        dbo.setPropertyValue(name(), ctx.event.guild.idLong, newSubChannelId)

        return EmbedUserSettings(ctx.event.author.asTag).setSetting(oldSubChannelId, newSubChannelId)
    }

    override fun delete(ctx: CommandContext): EmbedBuilder {
        val oldSubChannelId = dbo.getPropertyValue(name(), ctx.event.guild.idLong)

        dbo.setPropertyValue(name(), ctx.event.guild.idLong, "none")

        return EmbedUserSettings(ctx.event.author.asTag).setSetting(oldSubChannelId, "❌")
    }

    override fun showError(): EmbedBuilder =
        EmbedError().incorrectSetSetting(SUBCHANNEL_DOES_NOT_EXISTS)
}

class SubgroupSetting : IDiscordSetting {
    override fun name(): String =
        "subgroup"

    override fun info(): String =
        SUBGROUP_INFO

    override fun aliases(): List<String> =
        listOf("group", "gr", "g")

    override fun get(ctx: CommandContext): EmbedBuilder {
        val title = "Привязанная группа"
        val subGroup = dbo.getPropertyValue(name(), ctx.event.guild.idLong).replace("none", "Не указана")
        val prefix = dbo.getPropertyValue("prefix", ctx.event.guild.idLong)

        val fields = listOf<MessageEmbed.Field>(
            MessageEmbed.Field("Для привязки группы: ", "`${prefix}settings subgroup [ГРУППА]`", false),
            MessageEmbed.Field("Для отмены привязки: ", "`${prefix}settings subgroup delete`", false)
        )

        return EmbedUserSettings(ctx.event.author.asTag).getSetting(title, subGroup, fields)
    }

    override fun set(ctx: CommandContext): EmbedBuilder {
        if (calledDeleteOption(ctx.args[1]))
            return delete(ctx)

        val oldSubGroup = dbo.getPropertyValue(name(), ctx.event.guild.idLong)
        val requiredSubGroup = ctx.args[1]
        if (requiredSubGroup.isEmpty())
            return showError()

        val group = GroupFinder.execute(ctx.args[1])
        if (group == "none")
            return showError()

        dbo.setPropertyValue(name(), ctx.event.guild.idLong, group)

        return EmbedUserSettings(ctx.event.author.asTag).setSetting(oldSubGroup, group)
    }

    override fun delete(ctx: CommandContext): EmbedBuilder {
        val oldSubGroup = dbo.getPropertyValue(name(), ctx.event.guild.idLong)

        dbo.setPropertyValue(name(), ctx.event.guild.idLong, "none")

        return EmbedUserSettings(ctx.event.author.asTag).setSetting(oldSubGroup, "❌")
    }

    override fun showError(): EmbedBuilder =
        EmbedError().incorrectSetSetting(SUBGROUP_DOES_NOT_EXISTS)
}