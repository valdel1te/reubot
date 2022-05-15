package view.platforms.discord.logic.services.embeds

import net.dv8tion.jda.api.EmbedBuilder
import view.botservices.CommandInfoReader
import view.botservices.DateFormatter
import view.botservices.DiscordCommand
import java.util.stream.Collectors

object EmbedHelp {
    private fun formatHelpString(command: DiscordCommand): String =
        command.help.replace(";", ".\n")

    fun commandInfo(command: DiscordCommand): EmbedBuilder =
        EmbedBuilder().apply {
            setColor(HELP_COLOR)
            setTitle("Справочная помощь по команде")
            setDescription("${command.name} (${command.commandName})")
            addField("Назначение", formatHelpString(command), false)
            addField("Пример использования", "`${command.example}`", false)
            addField("Сокращения", command.aliases.joinToString(", "), false)
            if (command.needAdminRole)
                addField("Требуются права администратора!", "Иначе команда не будет исполнена", false)
            setFooter("Используйте «help» или «h» для получения всех команд | ${DateFormatter.today()}", REU_ICON)
        }

    fun allCommandsInfo(commands: List<DiscordCommand> = CommandInfoReader().getAllDiscordCommands()): EmbedBuilder =
        EmbedBuilder().apply {
            setColor(HELP_COLOR)
            setTitle("Все доступные команды")
            setDescription("Используйте «help» или «h» + `имя команды` для получения справочной информации по ней")

            val categories: List<String> = commands
                .stream()
                .map(DiscordCommand::category)
                .distinct()
                .collect(Collectors.toList())

            categories.forEach { category ->
                addField(
                    category,
                    commands.filter { command -> command.category == category }
                        .stream()
                        .map(DiscordCommand::name)
                        .collect(Collectors.toList())
                        .joinToString("\n"),
                    false
                )
            }

            setFooter("Никогда не доверяйте ботам в смешных шапках и очках | ${DateFormatter.today()}", REU_ICON)
        }
}