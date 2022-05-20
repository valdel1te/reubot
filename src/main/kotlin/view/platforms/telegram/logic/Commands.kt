package view.platforms.telegram.logic

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.dispatcher.handlers.TextHandlerEnvironment
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.KeyboardReplyMarkup
import com.github.kotlintelegrambot.entities.ParseMode
import model.data.entity.Subscribe
import view.botservices.GroupFinder
import view.botservices.database.TelegramDBO
import view.botservices.database.TelegramValues

private val buttonsGenerator = Buttons()
private val dbo = TelegramDBO()
private val telegram = TelegramValues()

private fun userAreSub(chatId: Long): Boolean =
    when (dbo.getPropertyValue(telegram.SUB_ENABLED, chatId)) {
        "+" -> true
        else -> false
    }

fun startCommand(chatId: Long, bot: Bot) {
    if (!dbo.clientIsExists(chatId))
        dbo.addClient(chatId)

    val keyboardMarkup = KeyboardReplyMarkup(
        keyboard = buttonsGenerator.startMenu(userAreSub(chatId)),
        resizeKeyboard = true,
        oneTimeKeyboard = false
    )

    bot.sendMessage(
        chatId = ChatId.fromId(chatId),
        text = "Выберите одно из предложенных действий в панели снизу",
        replyMarkup = keyboardMarkup
    )
}

fun subToGroupTextCommand(handler: TextHandlerEnvironment) {
    val keyboardMarkup = KeyboardReplyMarkup(
        keyboard = buttonsGenerator.allGroups(),
        resizeKeyboard = true,
        oneTimeKeyboard = false
    )

    handler.bot.sendMessage(
        chatId = ChatId.fromId(handler.message.chat.id),
        text = "Выберите или напишите требуемую группу для настройки",
        replyMarkup = keyboardMarkup
    )
}

fun backToStartTextCommand(handler: TextHandlerEnvironment) {
    startCommand(handler.message.chat.id, handler.bot)
}

fun groupSettingsTextEvent(handler: TextHandlerEnvironment) {
    val chatId = handler.message.chat.id

    val groupName = GroupFinder.execute(handler.text)
    if (groupName == "none") {
        handler.bot.sendMessage(
            ChatId.fromId(chatId),
            "Не могу найти группу..\nПопробуйте ещё раз"
        )
        return
    }

    val subscribeStatusRow: Subscribe = dbo.getSubscribeRecord(chatId, groupName);

    val inlineMarkup = buttonsGenerator.groupSettingsList(subscribeStatusRow)

    handler.bot.sendMessage(
        chatId = ChatId.fromId(chatId),
        text = "Ваши настройки для группы *$groupName*",
        parseMode = ParseMode.MARKDOWN,
        replyMarkup = inlineMarkup
    )
}