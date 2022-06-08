package view.platforms.telegram.logic

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.dispatcher.handlers.CallbackQueryHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.handlers.MessageHandlerEnvironment
import com.github.kotlintelegrambot.entities.CallbackQuery
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import model.data.entity.Subscribe
import view.botservices.DateFormatter
import view.botservices.database.TelegramDBO
import view.botservices.database.TelegramValues
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.LocalTime

private val buttonsGenerator = Buttons()
private val dbo = TelegramDBO()
private val telegram = TelegramValues()

private fun cbChatId(callbackQuery: CallbackQuery): Long? =
    callbackQuery.message?.chat?.id

private fun editKeyboard(
    bot: Bot,
    chatId: Long,
    messageId: Long,
    replyMarkup: InlineKeyboardMarkup
) =
    bot.editMessageReplyMarkup(
        chatId = ChatId.fromId(chatId),
        messageId = messageId,
        replyMarkup = replyMarkup
    )

private fun saveEditSubRecord(subRecord: Subscribe, chatId: Long) =
    dbo.editSubRecord(subRecord, chatId)

private fun createIfNotExists(chatId: Long, groupName: String) {
    if (dbo.getSubscribeRecord(chatId, groupName).group == "none")
        dbo.createSubscribeRecord(chatId, dbo.platformName(), groupName)
}

fun switchSubOffToOn(handler: CallbackQueryHandlerEnvironment, groupName: String) {
    val chatId = cbChatId(handler.callbackQuery) ?: return

    createIfNotExists(chatId, groupName)

    val messageId = handler.update.callbackQuery!!.message!!.messageId
    val newKeyboardMarkup = buttonsGenerator.groupSettingsList(dbo.getSubscribeRecord(chatId, groupName))

    editKeyboard(handler.bot, chatId, messageId, newKeyboardMarkup)

    handler.bot.sendMessage(ChatId.fromId(chatId), "Отслеживание группы $groupName успешно включено!")
}

fun switchSubOnToOff(handler: CallbackQueryHandlerEnvironment, groupName: String) {
    val chatId = cbChatId(handler.callbackQuery) ?: return

    dbo.deleteSubRecord(chatId, dbo.platformName(), groupName)

    val messageId = handler.update.callbackQuery!!.message!!.messageId
    val newKeyboardMarkup = buttonsGenerator.groupSettingsList(dbo.getSubscribeRecord(chatId, groupName))

    editKeyboard(handler.bot, chatId, messageId, newKeyboardMarkup)

    handler.bot.sendMessage(ChatId.fromId(chatId), "Отслеживание группы $groupName успешно отключено!")
}

fun switchUpdates(handler: CallbackQueryHandlerEnvironment, groupName: String) {
    val chatId = cbChatId(handler.callbackQuery) ?: return

    createIfNotExists(chatId, groupName)

    val subRecord = dbo.getSubscribeRecord(chatId, groupName).apply {
        getUpdate = !getUpdate // if true = false, else if false = true
    }

    saveEditSubRecord(subRecord, chatId)

    val messageId = handler.update.callbackQuery!!.message!!.messageId
    val newKeyboardMarkup = buttonsGenerator.groupSettingsList(subRecord)

    editKeyboard(handler.bot, chatId, messageId, newKeyboardMarkup)

    val status =
        if (subRecord.getUpdate)
            "включено"
        else
            "отключено"

    handler.bot.sendMessage(ChatId.fromId(chatId), "Отслеживание обновлений для группы $groupName успешно $status!")
}

fun editTime(
    handler: MessageHandlerEnvironment,
    groupName: String,
    subTime: String,
    messageId: Long
) {
    val chatId = handler.message.chat.id

    createIfNotExists(chatId, groupName)

    val subRecord = dbo.getSubscribeRecord(chatId, groupName).apply {
        time = DateFormatter.convertToTime(subTime)
    }

    saveEditSubRecord(subRecord, chatId)

    val newKeyboardMarkup = buttonsGenerator.groupSettingsList(subRecord)

    editKeyboard(handler.bot, chatId, messageId, newKeyboardMarkup)

    handler.bot.sendMessage(ChatId.fromId(chatId), "Для группы $groupName успешно задано время $subTime!")
}