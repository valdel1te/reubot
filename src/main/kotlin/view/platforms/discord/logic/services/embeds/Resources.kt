package view.platforms.discord.logic.services.embeds

import java.awt.Color

const val REU_ICON =
    "https://cdn.discordapp.com/attachments/626093012317110273/695312495585263646/D093D0B5D180D0B1_D0A0D0ADD0A3_D0B8D0BC_D09FD0BBD0B5D185D0B0D0BDD0BED0B2D0B0.png"

val ERROR_COLOR: Color = Color.RED
val SCHEDULE_COLOR: Color = Color(0xdff5d5)
val HELP_COLOR: Color = Color.GREEN
val SETTINGS_COLOR: Color = Color(0xff52cb)

const val ERROR_INCORRECT_INPUT = "Ошибка -> Некорректные введенные данные"

const val REASON_ERROR_PREFIX_LENGTH =
    "Длина префикса должна составлять 1-3 символа\n" +
            "Пример: `settings prefix !!`"
const val SUBCHANNEL_DOES_NOT_EXISTS =
    "Не удалось обнаружить текстовый канал с указанным ИД\n" +
            "Пример: `settings subchannel #updates`"
const val SUBGROUP_DOES_NOT_EXISTS =
    "Не удалось обнаружить указанную группу\n" +
            "Пример: `settings subgroup пко-41`"

const val PREFIX_INFO =
    "Специальный символ или группа символов, при указании которого бот понимает, что вызывается команда"
const val SUBCHANNEL_INFO =
    "Текстовый канал, куда будут отправляться обновления расписания на выбранную группу (subgroup)"
const val SUBGROUP_INFO =
    "Группа, за обновлениями расписания которой бот будет следить, также при вызове команды `schedule` без параметров, выведет расписание указанной группы"