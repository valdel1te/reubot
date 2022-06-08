package view.botservices

import java.sql.Time
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateFormatter {
    private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

    fun today(): String =
        LocalDateTime.now().format(formatter)

    fun format(date: String): String =
        LocalDate.parse(date).format(formatter)

    fun convertToTime(date: String): java.sql.Time =
        Time(SimpleDateFormat("HH:mm").parse(date).time)

    fun formatToTime(input: String): String {
        val formatInput = input.filter { char -> char.isDigit() }

        if (formatInput.isEmpty() || formatInput.length < 3)
            return "error"

        val hours: Int
        val minutes: Int

        if (formatInput.length < 4) {
            hours = formatInput[0].toString().toInt()
            minutes = formatInput.substring(1, 3).toInt()
        } else {
            hours = formatInput.substring(0, 2).toInt()
            minutes = formatInput.substring(2, 4).toInt()
        }

        if (hours > 23 || minutes > 59)
            return "error"

        return if (hours < 10)
            "0$hours:$minutes"
        else
            "$hours:$minutes"
    }
}