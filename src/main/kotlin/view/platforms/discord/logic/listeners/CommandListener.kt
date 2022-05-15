package view.platforms.discord.logic.listeners

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.hibernate.SessionFactory
import view.botservices.DatabaseOperations
import view.platforms.discord.logic.services.commandmanagment.CommandManager

class CommandListener() : ListenerAdapter() {
    private val manager = CommandManager()

    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (isBotOrWebhook(event))
            return

        val content = event.message.contentRaw
        val prefix = DatabaseOperations()
            .getDiscordPropertyValue("prefix", event.guild.idLong)

        if (content.startsWith(prefix)) {
            manager.handle(event, prefix)
        }
    }

    private fun isBotOrWebhook(event: MessageReceivedEvent): Boolean =
        event.author.isBot || event.isWebhookMessage
}