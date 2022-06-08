package view.platforms.telegram

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.message
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.ParseMode
import org.slf4j.LoggerFactory
import view.botservices.DateFormatter
import view.botservices.GetProperty
import view.platforms.telegram.logic.*

object TelegramBot {
    private val log = LoggerFactory.getLogger(TelegramBot::class.java)
    private val names = CommandNames()
    private val cbNames = CallbackNames()

    private val botToken = GetProperty.key("telegram_token")
    private const val botName = "ReuTelegram"
    private val bot: Bot by lazy(::getTelegramBot)

    private val usersRequests = hashMapOf<Long, String>()
    private val usersTimeStates = hashMapOf<Long, TimeProcessState>()

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
                command(names.START) {
                    startCommand(this.message.chat.id, this.bot)
                }

                text(names.SUB_TO_GROUP) {
                    subToGroupTextCommand(this)
                }
                text(names.GET_SCHEDULE) {
                    defineGroupForScheduleTextCommand(this)
                }
                text(names.TURN_BACK) {
                    backToStartTextCommand(this)
                    usersRequests.remove(message.chat.id)
                    usersTimeStates.remove(message.chat.id)
                }

                callbackQuery(cbNames.SUB_OFF) {
                    val chatId = callbackQuery.message?.chat?.id
                    switchSubOffToOn(this, usersRequests[chatId] ?: error(chatId!!))
                }
                callbackQuery(cbNames.SUB_ON) {
                    val chatId = callbackQuery.message?.chat?.id
                    switchSubOnToOff(this, usersRequests[chatId] ?: error(chatId!!))
                }
                callbackQuery(cbNames.UPDATES_EDIT) {
                    val chatId = callbackQuery.message?.chat?.id
                    switchUpdates(this, usersRequests[chatId] ?: error(chatId!!))
                }
                callbackQuery(cbNames.TIME_EDIT) {
                    val chatId = callbackQuery.message?.chat?.id
                    bot.sendMessage(
                        ChatId.fromId(chatId!!),
                        "Укажите время, в которое хотите получать расписание занятий (в формате *ЧАСЫ-МИНУТЫ*, к примеру 08-30, 9 45 или 19:55)",
                        ParseMode.MARKDOWN
                    )
                    usersTimeStates[chatId] = TimeProcessState.AskingForSubTime(callbackQuery.message!!.messageId)
                }

                text {
                    val chatId = message.chat.id
                    val group = messageGroupOrEmpty(this)

                    if (group.isEmpty()) {
                        bot.sendMessage(
                            ChatId.fromId(chatId),
                            "Не могу найти группу..\nПопробуйте ещё раз"
                        )
                        return@text
                    }

                    usersRequests[chatId] = group
                    usersTimeStates[chatId] = TimeProcessState.NotStarted
                    groupSettingsTextEvent(this, usersRequests[chatId] ?: error(chatId))
                }

                message {
                    when (val uts = usersTimeStates[message.chat.id]) {
                        is TimeProcessState.NotStarted -> Unit // skip message
                        is TimeProcessState.TimeReceived -> Unit // skip message
                        is TimeProcessState.AskingForSubTime -> {
                            val text = message.text ?: return@message

                            val time = DateFormatter.formatToTime(text)
                            if (time == "error") {
                                bot.sendMessage(
                                    ChatId.fromId(message.chat.id),
                                    "Не удалось обработать указанное время, попробуйте ещё раз.."
                                )
                                return@message
                            }

                            val messageId = uts.messageId
                            usersTimeStates[message.chat.id] = TimeProcessState.TimeReceived(time)
                            editTime(
                                this@message,
                                usersRequests[message.chat.id] ?: error(message.chat.id),
                                time,
                                messageId
                            )
                        }
                    }
                }
            }
        }

    private fun error(chatId: Long): Nothing {
        bot.sendMessage(ChatId.fromId(chatId), "Ошибка, пожалуйста, выберите группу заново..")
        throw NullPointerException("User $chatId caught a problem with group settings")
    }
}

sealed class TimeProcessState {
    object NotStarted : TimeProcessState()
    data class AskingForSubTime(val messageId: Long) : TimeProcessState()
    data class TimeReceived(val time: String) : TimeProcessState()
}