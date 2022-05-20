package view.platforms.telegram

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.text
import org.apache.log4j.lf5.LogLevel
import org.slf4j.LoggerFactory
import view.botservices.GetProperty
import view.platforms.telegram.logic.*

object TelegramBot {
    private val log = LoggerFactory.getLogger(TelegramBot::class.java)
    private val names = CommandNames()

    private val botToken = GetProperty.key("telegram_token")
    private const val botName = "ReuTelegram"
    private val bot: Bot by lazy(::getTelegramBot)

    fun start() {
        bot.startPolling()
        log.info("{} is ready", botName)
    }

    private fun stop() {
        bot.stopPolling()
        log.info("{} is shutting down", botName)
    }

    private fun getTelegramBot(): Bot =
        bot {
            token = botToken
            logLevel = com.github.kotlintelegrambot.logging.LogLevel.Error

            dispatch {
                command(names.START) { startCommand(this.message.chat.id, this.bot) }
                text(names.SUB_TO_GROUP) { subToGroupTextCommand(this) }
                text(names.TURN_BACK) { backToStartTextCommand(this) }

                text { groupSettingsTextEvent(this) }
            }
        }
}