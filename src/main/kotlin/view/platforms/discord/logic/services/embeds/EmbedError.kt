package view.platforms.discord.logic.services.embeds

import net.dv8tion.jda.api.EmbedBuilder
import view.botservices.DateFormatter

class EmbedError {
    fun incorrectSetSetting(reason: String): EmbedBuilder =
        EmbedBuilder().apply {
            setColor(ERROR_COLOR)
            setTitle(ERROR_INCORRECT_INPUT)
            addField("Причина:", reason, false)
            setFooter("Попробуйте ввести команду еще раз | ${DateFormatter.today()}")
        }
}