package view.platforms.discord.logic.commands.services

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.hibernate.SessionFactory
import view.platforms.discord.logic.commands.settings.GetConfig
import view.platforms.discord.logic.commands.settings.SetSettings

class CommandManager {
    private val commands = arrayListOf<Command>()

    init {
        addCommand(GetConfig())
        addCommand(SetSettings())
    }

    private fun addCommand(command: Command) {
        val nameExists = commands.stream().anyMatch { alreadyCreatedCommand ->
            alreadyCreatedCommand.getName().lowercase() == command.getName().lowercase()
        }

        if (nameExists)
            throw IllegalArgumentException("A command with this name already exists")

        commands.add(command)
    }

    private fun getCommand(search: String): Command? =
        commands.firstOrNull { command ->
            command.getName().lowercase() == search.lowercase()
        }

    fun handle(event: MessageReceivedEvent, prefix: String, sessionFactory: SessionFactory) {
        val splitContent = event.message.contentRaw
            .replaceFirst(prefix, "")
            .split("\\s+".toRegex())

        val name = splitContent[0].lowercase()
        val command = getCommand(name) ?: return

        val args = splitContent.subList(1, splitContent.size)
        val ctx = CommandContext(event, args, sessionFactory)

        if (command.needAdministratorPermission() && ctx.authorIsNotAdmin())
            return

        event.channel.sendTyping().queue()
        command.handle(ctx)
    }
}