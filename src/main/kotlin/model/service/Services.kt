package model.service

import abstracthibernate.AbstractHibernateService

import model.data.dao.ClientDao
import model.data.dao.PlatformDao
import model.data.dao.PlatformPropertyDao
import model.data.dao.PropertyDao
import model.data.entity.Client
import model.data.entity.Platform
import model.data.entity.PlatformProperty
import model.data.entity.Property
import org.hibernate.SessionFactory

class ClientService(sessionFactory: SessionFactory) :
    AbstractHibernateService<Client, Long>(ClientDao(sessionFactory))

class PlatformService(sessionFactory: SessionFactory) :
    AbstractHibernateService<Platform, Long>(PlatformDao(sessionFactory))

class PropertyService(sessionFactory: SessionFactory) :
    AbstractHibernateService<Property, Long>(PropertyDao(sessionFactory))

class PlatformPropertyService(sessionFactory: SessionFactory) :
    AbstractHibernateService<PlatformProperty, Long>(PlatformPropertyDao(sessionFactory))