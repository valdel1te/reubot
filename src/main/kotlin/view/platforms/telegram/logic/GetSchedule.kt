package view.platforms.telegram.logic

import kotlinx.coroutines.runBlocking
import view.botservices.schedule.*

class GetSchedule(private val groups: List<String>) {
    fun sendSchedule(date: String): List<String> {
        val sb = StringBuilder()
        val scheduleList = mutableListOf<String>()

        groups.forEach { group ->
            val schedule: Schedule
            runBlocking {
                schedule = ScheduleApiService().getSchedule(group, date)
            }

            sb.append("*$group, $date*\n\n")

            schedule.lessons.zip(TIMINGS).forEach { pair ->
                val timing = pair.second
                val lesson = LessonMapper()
                    .perform(pair.first, LessonToString()::single, LessonToString()::pair)
                    .replace("-", "")
                    .replace("+", "➕")

                sb.append("`[$timing]` |  $lesson\n")
            }

            scheduleList.add(sb.toString().trimIndent())
        }

        return scheduleList.toList()
    }
}