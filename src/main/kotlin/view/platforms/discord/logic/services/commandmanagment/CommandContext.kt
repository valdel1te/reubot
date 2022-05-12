package view.platforms.discord.logic.services.commandmanagment

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.hibernate.SessionFactory

class CommandContext(
    val event: MessageReceivedEvent,
    val args: List<String>,
    val sessionFactory: SessionFactory
) {

    fun authorIsNotAdmin(): Boolean =
        !this.event.guild.retrieveMember(this.event.author)
            .complete()
            .permissions.contains(Permission.ADMINISTRATOR)
}