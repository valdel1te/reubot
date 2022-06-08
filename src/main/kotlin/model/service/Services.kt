package model.service

import abstracthibernate.AbstractHibernateService

import model.data.dao.*
import model.data.entity.*
import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration

object BuildSession {
    private val sessionFactory = Configuration().configure().buildSessionFactory()

    fun build(): SessionFactory =
        sessionFactory
}

private val SESSION: SessionFactory = BuildSession.build()

class ClientService() :
    AbstractHibernateService<Client, Long>(ClientDao(SESSION)) {
    private val clientDao = ClientDao(SESSION)

    fun getByChatId(chatId: Long): Client =
        clientDao.getByChatId(chatId)
}

class PlatformService() :
    AbstractHibernateService<Platform, Long>(PlatformDao(SESSION)) {

    private val platformDao = PlatformDao(SESSION)

    fun getByName(name: String): Platform =
        platformDao.getByName(name)
}

class PropertyService() :
    AbstractHibernateService<Property, Long>(PropertyDao(SESSION)) {

    private val propertyDao = PropertyDao(SESSION)

    fun getPropertiesValues(clientId: Long): List<HashMap<String, String>> =
        propertyDao.getPropertiesValues(clientId)

    fun getValueByClientAndPlatformPropId(clientId: Long, platformPropId: Long): String =
        propertyDao.getValueByClientAndPlatformPropId(clientId, platformPropId)

    fun updateValueByClientAndPlatformPropId(clientId: Long, platformPropId: Long, newValue: String) =
        propertyDao.updateValueByClientAndPlatformPropId(clientId, platformPropId, newValue)
}

class PlatformPropertyService() :
    AbstractHibernateService<PlatformProperty, Long>(PlatformPropertyDao(SESSION)) {

    private val platformPropertyDao = PlatformPropertyDao(SESSION)

    fun getByName(name: String): PlatformProperty =
        platformPropertyDao.getByName(name)
}

class SubscribeService() :
    AbstractHibernateService<Subscribe, Long>(SubscribeDao(SESSION)) {

    private val subscribeDao = SubscribeDao(SESSION)

    fun getRecord(
        clientId: Long,
        platformId: Long,
        groupName: String
    ): Subscribe =
        subscribeDao.getRecord(clientId, platformId, groupName)

    fun getClientSubRecords(clientId: Long, platformId: Long): List<Subscribe> =
        subscribeDao.getClientSubRecords(clientId, platformId)
}