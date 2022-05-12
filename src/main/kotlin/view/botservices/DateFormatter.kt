package view.botservices

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateFormatter {
    private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

    fun today(): String =
        LocalDateTime.now().format(formatter)

    fun format(date: String): String =
        LocalDate.parse(date).format(formatter)
}