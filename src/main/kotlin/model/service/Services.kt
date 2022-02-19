package model.service

import abstracthibernate.AbstractHibernateService

import model.data.dao.*
import model.data.entity.*
import org.hibernate.SessionFactory

class ClientService(sessionFactory: SessionFactory) :
    AbstractHibernateService<Client, Long>(ClientDao(sessionFactory)) {
    private val clientDao = ClientDao(sessionFactory)

    fun getByChatId(chatId: Long): Client =
        clientDao.getByChatId(chatId)
}

class PlatformService(sessionFactory: SessionFactory) :
    AbstractHibernateService<Platform, Long>(PlatformDao(sessionFactory))

class PropertyService(sessionFactory: SessionFactory) :
    AbstractHibernateService<Property, Long>(PropertyDao(sessionFactory)) {

    private val propertyDao = PropertyDao(sessionFactory)

    fun getPropertiesValues(clientId: Long): List<HashMap<String, String>> =
        propertyDao.getPropertiesValues(clientId)

    fun getValueByClientAndPlatformPropId(clientId: Long, platformPropId: Long): String =
        propertyDao.getValueByClientAndPlatformPropId(clientId, platformPropId)
}

class PlatformPropertyService(sessionFactory: SessionFactory) :
    AbstractHibernateService<PlatformProperty, Long>(PlatformPropertyDao(sessionFactory)) {

    private val platformPropertyDao = PlatformPropertyDao(sessionFactory)

    fun getByName(name: String): PlatformProperty =
        platformPropertyDao.getByName(name)
}