import org.slf4j.LoggerFactory
import view.platforms.discord.DiscordBot
import view.platforms.telegram.TelegramBot
import java.io.File

fun main() {
    Config.start()

    //DiscordBot.start()
    TelegramBot.start()
}

object Config {
    val lessonsTime: List<String>
    val restAddress: String

    init {
        val text = File("service_cfg.txt").readLines(Charsets.UTF_8)

        restAddress = text.first { line -> line.startsWith("address=") }.substringAfter('=')

        val tempLessonsTime = mutableListOf<String>()
        text.subList(10, text.size).forEachIndexed { index, line ->
            if (line.startsWith("lessons_timings")) {
                tempLessonsTime.clear()
                tempLessonsTime.addAll(text.subList(index + 10 + 1, index + 10 + 8))
                return@forEachIndexed
            }
        }
        lessonsTime = tempLessonsTime
    }

    fun start() =
        LoggerFactory.getLogger(Config::class.java).info("Configuration ready")
}