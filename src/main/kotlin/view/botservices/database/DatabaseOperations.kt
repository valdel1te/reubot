package view.botservices.database

import model.data.entity.Subscribe
import model.service.*
import org.slf4j.Logger

interface DatabaseOperations {
    val clientService: ClientService
        get() = ClientService()
    val platformService: PlatformService
        get() = PlatformService()
    val propertyService: PropertyService
        get() = PropertyService()
    val platformPropertyService: PlatformPropertyService
        get() = PlatformPropertyService()
    val subscribeService: SubscribeService
        get() = SubscribeService()

    val logger: Logger

    fun platformName(): String

    fun addClient(chatId: Long)

    fun resetProperties(chatId: Long)

    fun setPropertyValue(
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

    fun getPropertyValue(
        propertyName: String,
        clientChatId: Long
    ): String {
        val platformPropertyId = platformPropertyService.getByName(propertyName).id!!
        val clientId = clientService.getByChatId(clientChatId).id!!

        return propertyService.getValueByClientAndPlatformPropId(clientId, platformPropertyId)
    }

    fun getClientProperties(clientChatId: Long): HashMap<String, String> {
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

    fun clientIsExists(chatId: Long): Boolean =
        clientService.exists("client_chat_id", chatId)

    fun getSubscribeRecord(
        chatId: Long,
        groupName: String
    ): Subscribe =
        subscribeService.getRecord(
            clientService.getByChatId(chatId).id ?: 0L,
            platformService.getIdByName(platformName()),
            groupName
        )
}