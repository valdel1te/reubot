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
    val UPDATES_ON: String = "\uD83D\uDFE2 Получение обновлений включено", // 🟢
    val CANCEL_SCHEDULE_SEARCH: String = "Отменить"
)

data class CallbackNames(
    val SUB_OFF: String = "sub_off",
    val SUB_ON: String = "sub_on",
    val UPDATES_EDIT: String = "updates_edit",
    val TIME_EDIT: String = "time_edit",
    val SUB_GROUP_SCHEDULE: String = "sub_group_schedule",
    val CANCEL_SCHEDULE_SEARCH: String = "cancel_schedule_search"
)