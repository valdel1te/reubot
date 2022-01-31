package view.platforms.discord.logic.commands.services

interface Command {
    fun handle(ctx: CommandContext)

    fun getName(): String

    fun getHelp(): String

    fun getAliases(): List<String> {
        return listOf()
    }
}