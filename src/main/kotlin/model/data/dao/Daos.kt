package model.data.dao

import abstracthibernate.AbstractHibernateDao
import model.data.entity.*
import org.hibernate.SessionFactory
import org.hibernate.transform.Transformers
import javax.persistence.NoResultException


class ClientDao(sessionFactory: SessionFactory) :
    AbstractHibernateDao<Client, Long>(sessionFactory, Client()) {

    fun getByChatId(chatId: Long): Client {
        try {
            sessionFactory.openSession().use { session ->
                val sql = "SELECT * FROM client WHERE client_chat_id = :id"
                val query = session.createNativeQuery(sql)
                    .addEntity(Client::class.java)
                    .setParameter("id", chatId)

                val client = query.singleResult as Client
                session.close()
                return client
            }
        } catch (e: NoResultException) {
            return Client().apply { clientChatId = 0L }
        }
    }
}

class PlatformDao(sessionFactory: SessionFactory) :
    AbstractHibernateDao<Platform, Long>(sessionFactory, Platform())

class PropertyDao(sessionFactory: SessionFactory) :
    AbstractHibernateDao<Property, Long>(sessionFactory, Property()) {

    fun getPropertiesValues(clientId: Long): List<HashMap<String, String>> {
        try {
            sessionFactory.openSession().use { session ->
                val sql = "SELECT new map (pp.propertyName, p.value) " +
                        "FROM Property p " +
                        "JOIN PlatformProperty pp ON p.platformPropId = pp.id " +
                        "AND p.clientId = $clientId"
                val query = session.createQuery(sql)

                return query.resultList as List<HashMap<String, String>>
            }
        } catch (e: NoResultException) {
            return emptyList()
        }
    }
}

class PlatformPropertyDao(sessionFactory: SessionFactory) :
    AbstractHibernateDao<PlatformProperty, Long>(sessionFactory, PlatformProperty()) {

    fun getByName(name: String): PlatformProperty {
        try {
            sessionFactory.openSession().use { session ->
                val sql = "SELECT * FROM platform_property WHERE property_name = :name"
                val query = session.createNativeQuery(sql)
                    .addEntity(PlatformProperty::class.java)
                    .setParameter("name", name)

                val platformProperty = query.singleResult as PlatformProperty
                session.close()
                return platformProperty
            }
        } catch (e: NoResultException) {
            return PlatformProperty().apply { propertyName = "none" }
        }
    }
}