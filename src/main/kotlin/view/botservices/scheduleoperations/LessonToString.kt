package view.botservices.scheduleoperations

class LessonToString {
    fun single(lesson: Lesson): String =
        "${lesson.name}, ${lesson.teacherName}, ${lesson.type}, ауд. ${printAuditorium(lesson.auditorium)}"

    fun pair(pair: Pair<Lesson?, Lesson?>): String {
        val firstLesson = pair.first
        val secondLesson = pair.second

        val firstLessonString =
            if (firstLesson is Lesson)
                single(firstLesson)
            else
                "-"

        val secondLessonString =
            if (secondLesson is Lesson)
                single(secondLesson)
            else
                "-"

        return "$firstLessonString\n+\n$secondLessonString"
    }

    private fun printAuditorium(auditorium: String): String {
        if (auditorium.isEmpty())
            return "(еще не указана)"
        return auditorium
    }
}