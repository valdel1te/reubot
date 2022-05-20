package view.platforms.discord.logic.services.embeds

import net.dv8tion.jda.api.EmbedBuilder
import view.botservices.schedule.*
import view.botservices.schedule.TIMINGS

class EmbedSchedule {
    fun withGroupAndDate(group: String, date: String, lessons: Array<Indivisible>): EmbedBuilder =
        EmbedBuilder().apply {
            setColor(SCHEDULE_COLOR)
            setAuthor("\uD83D\uDCDA РАСПИСАНИЕ")
            setThumbnail(REU_ICON)
            setTitle("Группа $group, $date")

            lessons.zip(TIMINGS).forEach { pair ->
                val lesson = pair.first
                val timing = pair.second
                addField(
                    "`$timing`",
                    LessonMapper()
                        .perform(lesson, LessonToString()::single, LessonToString()::pair)
                        .replace("-", "")
                        .replace("+", "➕"),
                    false
                )
            }

            setFooter("schedule rea") // todo написать про шапки
        }
}