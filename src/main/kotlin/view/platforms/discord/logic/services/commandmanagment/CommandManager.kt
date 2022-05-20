package view.platforms.discord.logic.services.commandmanagment

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import view.platforms.discord.logic.commands.schedule.GetSchedule
import view.platforms.discord.logic.commands.settings.GetConfig
import view.platforms.discord.logic.commands.settings.SetSettings
import view.platforms.discord.logic.services.embeds.EmbedHelp

class CommandManager {
    private val commands = arrayListOf<ICommand>()

    init {
        addCommand(GetConfig())
        addCommand(SetSettings())
        addCommand(GetSchedule())
    }

    private fun addCommand(command: ICommand) {
        val nameExists = commands.stream().anyMatch { alreadyCreatedCommand ->
            alreadyCreatedCommand.name().lowercase() == command.name().lowercase()
        }

        if (nameExists)
            throw IllegalArgumentException("A command with this name already exists")

        commands.add(command)
    }

    private fun getCommand(search: String): ICommand? =
        commands.firstOrNull { command ->
            command.name().lowercase() == search.lowercase() || command.aliases().stream().anyMatch { alias ->
                alias.lowercase() == search.lowercase()
            }
        }

    fun handle(event: MessageReceivedEvent, prefix: String) {
        val splitContent = event.message.contentRaw
            .replaceFirst(prefix, "")
            .split("\\s+".toRegex())

        val name = splitContent[0].lowercase()
        val channel = event.channel

        if (name == "help" || name == "h") {
            val index: Int = if (splitContent.size == 1)
                0
            else
                1

            val helpCommand = getCommand(splitContent[index]) ?: object: ICommand {
                override fun help(): EmbedBuilder =
                    EmbedHelp.allCommandsInfo()
            }

            callHelp(helpCommand, channel)
            return
        }

        val command = getCommand(name) ?: return

        val args = splitContent.subList(1, splitContent.size)
        val ctx = CommandContext(event, args)

        if (command.needAdministratorPermission() && ctx.authorIsNotAdmin())
            return

        channel.sendTyping().queue()
        command.handle(ctx)
    }

    private fun callHelp(command: ICommand, channel: MessageChannel) {
        channel.sendTyping().queue()
        channel.sendMessageEmbeds(command.help().build()).queue()
    }
}