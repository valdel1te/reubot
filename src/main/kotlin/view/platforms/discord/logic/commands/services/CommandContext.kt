package view.platforms.discord.logic.commands.services

import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.hibernate.SessionFactory

class CommandContext(
    val event: MessageReceivedEvent,
    val args: List<String>,
    val sessionFactory: SessionFactory
) {
    fun getGuild(): Guild =
        this.getGuild()
}