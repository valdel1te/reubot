package view.platforms.telegram.logic

data class CommandNames(
    val START: String = "start",
    val GET_SCHEDULE: String = "\uD83D\uDD3B Узнать расписание", //🔻
    val SUB_SETTINGS: String = "\uD83D\uDD3B Настройки подписки", //🔻
    val SUB_TO_GROUP: String = "\uD83D\uDD3B Подписаться на группу", //🔻
    val TURN_BACK: String = "\uD83D\uDD3A Вернуться назад", // 🔺
    val SUB_OFF: String = "\uD83D\uDD34 Отслеживание отключено", // 🔴
    val SUB_ON: String = "\uD83D\uDFE2 Отслеживание включено", // 🟢
    val UPDATES_OFF: String = "\uD83D\uDD34 Получение обновлений отключено", // 🔴
    val UPDATES_ON: String = "\uD83D\uDFE2 Получение обновлений включено" // 🟢
)

data class CallbackNames(
    val SUB_OFF: String = "sub_off",
    val SUB_ON: String = "sub_off",
    val UPDATES_OFF: String = "updates_off",
    val UPDATES_ON: String = "updates_on",
    val TIME_EDIT: String = "time_edit"
)