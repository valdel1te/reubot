package view.botservices.scheduleoperations

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

val jsonStrict = SerializersModule {
    polymorphic(LessonEntity::class) {
        subclass(EmptyLesson::class)
        subclass(Lesson::class)
    }
    polymorphic(Indivisible::class) {
        subclass(SingleLesson::class)
        subclass(PairLesson::class)
    }
}

val format = Json {
    serializersModule = jsonStrict
    prettyPrint = true
    ignoreUnknownKeys = true
    encodeDefaults = true
}

val TIMINGS = arrayListOf<String>(
    "08:30 - 10:00",
    "10:10 - 11:40",
    "12:10 - 13:40",
    "14:00 - 15:30",
    "15:40 - 17:10",
    "17:30 - 19:00",
    "19:10 - 20:40",
    "20:45 - 22:15"
)
//todo: список неполный
val GROUPS = arrayListOf<String>(
    "БДо-11",
    "Бо-11", "Бо-21", "Бо-31", "Бо-32",
    "Бс-11",
    "ММ-11",
    "МН-11", "МН-21",
    "МНв-11",
    "МНз-21",
    "МНи-21", "МНи-31",
    "ПКо-41",
    "ПР-11", "ПР-21", "ПР-22", "ПР-31",
    "ПРз-21", "ПРз-31",
    "СДо-11", "СДо-21",
    "СДс-11",
    "ТВ-11", "ТВ-21", "ТВ-31",
    "ТВз-21", "ТВз-31",
    "ТД-11", "ТД-21", "ТД-31",
    "ТДз-21", "ТДз-31",
    "То-21", "То-31",
    "ТП-11", "ТП-21", "ТП-31",
    "ТПз-21", "ТПз-31",
    "ТПи-31",
    "Тс-21",
    "ТЭо-11", "ТЭо-21",
    "ТЭс-11",
    "ЭК-11", "ЭК-21", "ЭК-31",
    "ЭКв-11", "ЭКв-31",
    "ЭКз-21", "ЭКз-22", "ЭКз-31", "ЭКз-32",
    "ЭКи-21", "ЭКи-31"
)

interface LessonEntity {
    fun isEmpty() = true
}

interface Indivisible {
    fun isIndivisible() = true
}

@Serializable
@SerialName("Empty")
class EmptyLesson() : LessonEntity {

    override fun toString() = "Empty"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

@Serializable
@SerialName("Lesson")
data class Lesson(
    val name: String,
    val teacherName: String,
    @SerialName("lessonType")
    val type: String = "",
    var auditorium: String = ""
) : LessonEntity {

    override fun isEmpty() = false
}

@Serializable
@SerialName("SingleLesson")
data class SingleLesson(
    val lesson: LessonEntity
) : Indivisible

@Serializable
@SerialName("PairLesson")
data class PairLesson(
    val pair: Pair<LessonEntity, LessonEntity>
) : Indivisible {

    override fun isIndivisible() = false
}

@Serializable
data class Schedule(
    var groupName: String,
    var date: String,
    var lessons: Array<Indivisible>
) {

    constructor() : this("", "", Array(8) {SingleLesson(EmptyLesson())})

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Schedule

        if (groupName != other.groupName) return false
        if (date != other.date) return false
        if (!lessons.contentEquals(other.lessons)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = groupName.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + lessons.contentHashCode()
        return result
    }
}