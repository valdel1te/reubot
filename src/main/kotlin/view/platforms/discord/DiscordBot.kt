package view.platforms.discord

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import org.hibernate.cfg.Configuration
import org.slf4j.LoggerFactory
import view.botservices.GetProperty
import view.platforms.discord.logic.listeners.CommandListener
import view.platforms.discord.logic.listeners.GuildListener

object DiscordBot {
    private val token = GetProperty.key("discord_token")
    private val jda: JDA = JDABuilder.createDefault(token).build()
    private val logger = LoggerFactory.getLogger(DiscordBot::class.java)

    fun start() {
        val sessionFactory = Configuration().configure().buildSessionFactory()

        jda.run {
            presence.setStatus(OnlineStatus.ONLINE)
            presence.activity = Activity.watching("rea.perm")

            addEventListener(CommandListener(sessionFactory))
            addEventListener(GuildListener(sessionFactory))
        }

        logger.info("${jda.selfUser} STARTED")
    }

    fun stop() {
        jda.shutdown()
        logger.info("${jda.selfUser} STOPPED")
    }
}