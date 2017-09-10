package com.meysamasadian.treasury.dao.hibernate;

import com.meysamasadian.treasury.dao.DocumentDao;
import com.meysamasadian.treasury.model.Document;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by rahnema on 9/6/2017.
 */

@Repository("documentDao")
public class DocumentDaoImpl extends GenericDaoImpl<Document> implements DocumentDao {
    protected DocumentDaoImpl() {
        super(Document.class);
    }

    @Override
    public void save(Document document) {
        super.save(document);
    }

    @Override
    public void update(Document document) {
        super.update(document);
    }

    @Override
    public void delete(Document document) {
        super.delete(document);
    }

    @Override
    public Document load(long id) {
        return super.load(id);
    }

    @Override
    public Document loadLastOfDocument(String pan) {
        @SuppressWarnings("JpaQlInspection")
        String queryStr = "select doc from Document doc where doc.source.pan = :pan or doc.dest.pan = :pan order by doc.date desc";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr).setString("pan",pan);
        List<Document> documents = query.list();
        return documents != null && !documents.isEmpty() ? documents.get(0) : null;
    }
}
