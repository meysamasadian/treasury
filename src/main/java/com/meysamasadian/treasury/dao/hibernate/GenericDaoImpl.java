package com.meysamasadian.treasury.dao.hibernate;

import com.meysamasadian.treasury.dao.GenericDao;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by rahnema on 9/6/2017.
 */
@Transactional
public class GenericDaoImpl<T> implements GenericDao<T> {

    @Autowired
    protected SessionFactory sessionFactory;

    private Class<T> clz;

    protected GenericDaoImpl(Class<T> clz) {
        this.clz = clz;
    }

    @Override
    public void save(T t) {
        sessionFactory.getCurrentSession().save(t);
    }

    @Override
    public void update(T t) {
        sessionFactory.getCurrentSession().update(t);
    }

    @Override
    public void delete(T t) {
        sessionFactory.getCurrentSession().delete(t);
    }

    @Override
    public T load(long id) {
        return (T) sessionFactory.getCurrentSession().load(clz,id);
    }

}
