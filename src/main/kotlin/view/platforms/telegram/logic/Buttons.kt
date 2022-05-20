package view.platforms.telegram.logic

import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import com.github.kotlintelegrambot.entities.keyboard.KeyboardButton
import model.data.entity.Subscribe
import view.botservices.GroupFinder
import view.botservices.database.TelegramDBO

class Buttons {
    private val groups = GroupFinder.groups
    private val names = CommandNames()
    private val cbNames = CallbackNames()

    fun startMenu(subEnabled: Boolean): List<List<KeyboardButton>> =
        listOf(
            listOf(KeyboardButton(names.GET_SCHEDULE)),
            if (subEnabled)
                listOf(KeyboardButton(names.SUB_SETTINGS))
            else
                listOf(KeyboardButton(names.SUB_TO_GROUP)),
        )

    fun allGroups(): List<List<KeyboardButton>> {
        val buttons = mutableListOf<List<KeyboardButton>>()
        val row = mutableListOf<KeyboardButton>()

        buttons.add(listOf(KeyboardButton(names.TURN_BACK)))

        groups.forEach { group ->
            row.add(KeyboardButton(group))
            if (row.size == 4) { // max button count in a row = 8, but 8 it's too much for phone with small screen
                buttons.add(row.toList())
                row.clear()
            }
        }

        return buttons.toList()
    }

    fun groupSettingsList(subStatus: Subscribe): InlineKeyboardMarkup {
        val subStatusButton: Pair<String, String>
        val timeButton: Pair<String, String>
        val getUpdates: Pair<String, String>

        if (subStatus.group == "none") {
            subStatusButton = Pair(names.SUB_OFF, cbNames.SUB_OFF)
            timeButton = Pair("Время: не указано", cbNames.TIME_EDIT)
            getUpdates = Pair(names.UPDATES_OFF, cbNames.SUB_OFF)
        } else {
            subStatusButton = Pair(names.SUB_ON, cbNames.SUB_ON)
            timeButton = Pair("Время: ${subStatus.time ?: "не указано"}", cbNames.TIME_EDIT)
            getUpdates =
                if (subStatus.getUpdate)
                    Pair(names.UPDATES_ON, cbNames.UPDATES_ON)
                else
                    Pair(names.UPDATES_OFF, cbNames.UPDATES_OFF)
        }

        return InlineKeyboardMarkup.create(
            listOf(InlineKeyboardButton.CallbackData(subStatusButton.first, subStatusButton.second)),
            listOf(InlineKeyboardButton.CallbackData(timeButton.first, timeButton.second)),
            listOf(InlineKeyboardButton.CallbackData(getUpdates.first, getUpdates.second))
        )
    }
}