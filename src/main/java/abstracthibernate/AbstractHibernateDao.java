package abstracthibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;


public abstract class AbstractHibernateDao<Entity, Key extends Serializable> {

    protected final SessionFactory sessionFactory;

    private Entity entity;

    public AbstractHibernateDao(SessionFactory sessionFactory, Entity entity) {
        this.entity = entity;
        this.sessionFactory = sessionFactory;
    }

    public AbstractHibernateDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void create(Entity entity) {
        try (final Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(entity);
            session.getTransaction().commit();
        }
    }

    public void update(Entity client) {
        try (final Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(client);
            session.getTransaction().commit();
        }
    }

    public void delete(Entity entity) {
        try (final Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(entity);
            session.getTransaction().commit();
        }
    }

    public Entity read(Key key, Entity clazz) {
        try (final Session session = sessionFactory.openSession()) {
            Entity entity = (Entity) session.get(clazz.getClass(), key);
            return entity != null ? entity : (Entity) clazz.getClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Entity read(Key key) {
        try (final Session session = sessionFactory.openSession()) {
            Entity entity = (Entity) session.get(this.entity.getClass(), key);
            return entity != null ? entity : (Entity) this.entity.getClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Entity> getList(String name) {
        try (final Session session = sessionFactory.openSession()) {
            return session.getSession().createQuery("from " + name).list();
        }
    }

    public List<Entity> getList() {
        try (final Session session = sessionFactory.openSession()) {
            return session.getSession().createQuery("from " + entity.getClass().getName()).list();
        }
    }
}

