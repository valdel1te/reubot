package view.platforms.discord.logic.listeners

import net.dv8tion.jda.api.events.guild.GuildJoinEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.hibernate.SessionFactory
import view.botservices.DatabaseOperations

class GuildListener(private val sessionFactory: SessionFactory) : ListenerAdapter() {
    private val dbo = DatabaseOperations(sessionFactory)

    override fun onGuildJoin(event: GuildJoinEvent) {
        val guildClient = event.guild.idLong
        val text = "хай, я ржу бот, пиши -help и разбирайся че делать будем"

        event.guild.defaultChannel!!.sendMessage(text).queue()

        if (dbo.clientIsExists(guildClient))
            return

        dbo.addNewClient(guildClient)
    }
}