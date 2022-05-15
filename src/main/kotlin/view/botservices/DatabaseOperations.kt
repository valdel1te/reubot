package view.botservices

import model.data.entity.Client
import model.data.entity.Property
import model.service.ClientService
import model.service.PlatformPropertyService
import model.service.PlatformService
import model.service.PropertyService
import org.slf4j.LoggerFactory

class DatabaseOperations {
    private val clientService = ClientService()
    private val platformService = PlatformService()
    private val propertyService = PropertyService()
    private val platformPropertyService = PlatformPropertyService()

    private val logger = LoggerFactory.getLogger(DatabaseOperations::class.java)

    fun updateDiscordPropertyValue(
        propertyName: String,
        clientChatId: Long,
        newValue: String
    ) {
        val platformPropertyId = platformPropertyService.getByName(propertyName).id!!
        val clientId = clientService.getByChatId(clientChatId).id!!

        val oldValue = propertyService.getValueByClientAndPlatformPropId(clientId, platformPropertyId)

        propertyService.updateValueByClientAndPlatformPropId(clientId, platformPropertyId, newValue)
        logger.info("Client <$clientChatId> updated [${propertyName.uppercase()}]: [$oldValue] -> [$newValue]")
    }

    fun getDiscordPropertyValue(
        propertyName: String,
        clientChatId: Long
    ): String {
        val platformPropertyId = platformPropertyService.getByName(propertyName).id!!
        val clientId = clientService.getByChatId(clientChatId).id!!

        return propertyService.getValueByClientAndPlatformPropId(clientId, platformPropertyId)
    }

    fun getClientDiscordProperties(clientChatId: Long): HashMap<String, String> {
        val configList = HashMap<String, String>()
        val client = clientService.getByChatId(clientChatId)
        val config = propertyService.getPropertiesValues(client.id!!).toMutableList()

        if (client.id == 0L || config.isEmpty()) {
            config.apply {
                clear()
                add(hashMapOf("error" to "list are empty"))
            }
        }

        config.forEach { map ->
            val configRow = mutableListOf<String>()
            map.forEach { settingMap ->
                configRow.add(settingMap.value)
            }

            configList[configRow[0]] = configRow[1]
        }

        return configList
    }

    fun addNewClient(chatId: Long) {
        val client = Client().apply {
            clientChatId = chatId
        }

        clientService.create(client)
        logger.info("Created new client [${client.clientChatId}]")

        // base configuration for new client
        propertyService.apply {
            create(Property().apply {
                clientId = client
                platformPropId = platformPropertyService.getByName("prefix")
                value = "-"
            })
            create(Property().apply {
                clientId = client
                platformPropId = platformPropertyService.getByName("subchannel_id")
                value = "none"
            })
            create(Property().apply {
                clientId = client
                platformPropId = platformPropertyService.getByName("subgroup")
                value = "none"
            })
        }
    }

    fun clientIsExists(chatId: Long): Boolean =
        clientService.exists("client_chat_id", chatId)
}