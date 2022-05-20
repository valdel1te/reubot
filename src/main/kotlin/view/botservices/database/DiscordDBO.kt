package view.botservices.database

import model.data.entity.Client
import model.data.entity.Property
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DiscordDBO : DatabaseOperations {
    private val discord = DiscordValues()

    override val logger: Logger =
        LoggerFactory.getLogger(DiscordDBO::class.java)

    override fun platformName(): String =
        "discord"

    override fun addClient(chatId: Long) {
        val client = Client().apply {
            clientChatId = chatId
        }

        clientService.create(client)
        logger.info("Created new client [${client.clientChatId}]")

        // base configuration for new client
        propertyService.apply {
            create(Property().apply {
                clientId = client
                platformPropId = platformPropertyService.getByName(discord.PREFIX)
                value = "-"
            })
            create(Property().apply {
                clientId = client
                platformPropId = platformPropertyService.getByName(discord.SUBCHANNEL_ID)
                value = "none"
            })
            create(Property().apply {
                clientId = client
                platformPropId = platformPropertyService.getByName(discord.SUBGROUP)
                value = "none"
            })
        }
    }

    override fun resetProperties(chatId: Long) {
        setPropertyValue(
            discord.PREFIX,
            chatId,
            "-"
        )
        setPropertyValue(
            discord.SUBCHANNEL_ID,
            chatId,
            "none"
        )
        setPropertyValue(
            discord.SUBGROUP,
            chatId,
            "none"
        )
    }
}