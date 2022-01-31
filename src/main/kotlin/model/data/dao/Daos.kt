package model.data.dao

import abstracthibernate.AbstractHibernateDao
import model.data.entity.Client
import model.data.entity.Platform
import model.data.entity.PlatformProperty
import model.data.entity.Property
import org.hibernate.SessionFactory

class ClientDao(sessionFactory: SessionFactory) :
    AbstractHibernateDao<Client, Long>(sessionFactory, Client())

class PlatformDao(sessionFactory: SessionFactory) :
    AbstractHibernateDao<Platform, Long>(sessionFactory, Platform())

class PropertyDao(sessionFactory: SessionFactory) :
    AbstractHibernateDao<Property, Long>(sessionFactory, Property())

class PlatformPropertyDao(sessionFactory: SessionFactory) :
    AbstractHibernateDao<PlatformProperty, Long>(sessionFactory, PlatformProperty())