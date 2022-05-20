package view.botservices.schedule

class LessonMapper {
    fun perform(
        indivisible: Indivisible,
        singleOperation: (Lesson) -> String,
        pairOperation: (Pair<Lesson?, Lesson?>) -> String
    ): String {
        if (indivisible is SingleLesson) {
            val entity = indivisible.lesson

            if (entity.isEmpty())
                return "-"

            if (entity is Lesson)
                return singleOperation(entity)
        }

        if (indivisible is PairLesson) {
            val entity = indivisible.pair

            return pairOperation(
                Pair(
                    lessonEntityToNull(entity.first),
                    lessonEntityToNull(entity.second)
                )
            )
        } else
            return ""
    }

    private fun lessonEntityToNull(entity: LessonEntity): Lesson? {
        if (entity is Lesson)
            return entity
        return null
    }
}