package abstracthibernate;

import java.io.Serializable;
import java.util.List;

public abstract class AbstractHibernateService<Entity, Id extends Serializable> {
    protected AbstractHibernateDao<Entity, Id> dao;

    public AbstractHibernateService(AbstractHibernateDao<Entity, Id> dao) {
        this.dao = dao;
    }

    public void create(Entity entity) {
        dao.create(entity);
    }

    public Entity read(Id id) {
        return dao.read(id);
    }

    public void update(Entity entity) {
        dao.update(entity);
    }

    public void delete(Entity entity) {
        dao.delete(entity);
    }

    public List<Entity> getList() {
        return dao.getList();
    }

    public boolean exists(String attribute, Long value) {
        return dao.exists(attribute, value);
    }

    public boolean exists(String attribute, String value) {
        return dao.exists(attribute, value);
    }
}

