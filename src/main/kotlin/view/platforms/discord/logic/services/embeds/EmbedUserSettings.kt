package view.platforms.discord.logic.services.embeds

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import view.botservices.DateFormatter
import view.botservices.IDiscordSetting

class EmbedUserSettings(private val authorMessage: String) {
    fun userAllSettings(
        settings: String,
        guild: String,
    ): EmbedBuilder =
        EmbedBuilder().apply {
            setColor(SETTINGS_COLOR)
            setAuthor("Настройки $guild")
            setTitle("Для уточения значения настроек указывайте команду `config info`")
            addField("", settings, false)
            setFooter("Запрошено пользователем $authorMessage | ${DateFormatter.today()}")
        }

    fun configInfo(settings: HashMap<String, IDiscordSetting>): EmbedBuilder =
        EmbedBuilder().apply {
            setColor(SETTINGS_COLOR)
            setTitle("Все возможные настройки")
            for ((name, command) in settings) {
                val aliases = command.aliases().joinToString(",")
                addField(name, "${command.info()}\nСокращения: $aliases", false)
            }
            setFooter("Запрошено пользователем $authorMessage | ${DateFormatter.today()}")
        }

    fun getSetting(title: String, description: String, fields: List<MessageEmbed.Field>): EmbedBuilder =
        EmbedBuilder().apply {
            setColor(SETTINGS_COLOR)
            setTitle(title)
            setDescription(description)
            fields.forEach { field -> addField(field) }
            setFooter("Запрошено пользователем $authorMessage | ${DateFormatter.today()}")
        }

    fun setSetting(old: String, new: String): EmbedBuilder =
        EmbedBuilder().apply {
            setColor(SETTINGS_COLOR)
            setTitle("Настройка успешно обновлена")
            setDescription("`$old` -> `$new`")
            setFooter("Запрошено пользователем $authorMessage | ${DateFormatter.today()}")
        }
}