package view.botservices.database

import model.data.entity.Client
import model.data.entity.Property
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class TelegramDBO : DatabaseOperations {
    private val telegram = TelegramValues()

    override val logger: Logger =
        LoggerFactory.getLogger(TelegramDBO::class.java)

    override fun platformName(): String =
        "telegram"

    override fun addClient(chatId: Long) {
        val client = Client().apply {
            clientChatId = chatId
        }

        clientService.create(client)

        propertyService.apply {
            create(Property().apply {
                clientId = client
                platformPropId = platformPropertyService.getByName(telegram.SUB_ENABLED)
                value = "-"
            })
        }

        logger.info("Created new client [${client.clientChatId}]")
    }

    override fun resetProperties(chatId: Long) {
        setPropertyValue(
            telegram.SUB_ENABLED,
            chatId,
            "-"
        )
    }
}