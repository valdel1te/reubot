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
            platformService.getByName(platformName()).id ?: 0L,
            groupName
        )

    fun createSubscribeRecord(
        chatId: Long,
        platformName: String,
        groupName: String
    ) {
        val client = clientService.getByChatId(chatId)
        val platform = platformService.getByName(platformName)

        val newSubRecord = Subscribe().apply {
            this.clientId = client
            platformId = platform
            group = groupName
            time = null
            getUpdate = false
        }

        subscribeService.create(newSubRecord)

        logger.info("Client <$chatId> sub-ed [${groupName.uppercase()}]: [TIME:null] [GET_UPD:false]")
    }

    fun editSubRecord(subRecord: Subscribe, chatId: Long) {
        subscribeService.update(subRecord)

        logger.info(
            "Client <$chatId> update settings [${subRecord.group.uppercase()}]: [TIME:${subRecord.time}] [GET_UPD:${subRecord.getUpdate}]"
        )
    }

    fun deleteSubRecord(
        chatId: Long,
        platformName: String,
        groupName: String
    ) {
        val clientId = clientService.getByChatId(chatId).id!!
        val platformId = platformService.getByName(platformName).id!!

        val subRecord = subscribeService.getRecord(clientId, platformId, groupName)

        subscribeService.delete(subRecord)

        logger.info("Client <$chatId> unsub-ed [${groupName.uppercase()}]")
    }

    fun listClientSubRecord(chatId: Long, platformName: String): List<Subscribe> =
        subscribeService.getClientSubRecords(
            clientService.getByChatId(chatId).id!!,
            platformService.getByName(platformName).id!!
        )
}