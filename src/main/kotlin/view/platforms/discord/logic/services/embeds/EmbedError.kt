package view.platforms.discord.logic.services.embeds

import net.dv8tion.jda.api.EmbedBuilder

class EmbedError {
    fun incorrectSettingsValue(
        settingType: String,
        errorReason: String,
        incorrectUserSetting: String,
        correctSettingExample: String,
    ): EmbedBuilder =
        EmbedBuilder().apply {
            setColor(ERROR_COLOR)
            setTitle(ERROR_INCORRECT_INPUT)
            setDescription("Настройки -> $settingType")

        }

    fun incorrectSettingsValue(
        settingType: String,
        errorReason: String,
        incorrectUserSetting: String,
        correctSettingExample: String,
        hints: Array<String>
    ): EmbedBuilder =
        EmbedBuilder().apply {
            setColor(ERROR_COLOR)
            setTitle(ERROR_INCORRECT_INPUT)
            setDescription("Настройки -> $settingType")

        }
}