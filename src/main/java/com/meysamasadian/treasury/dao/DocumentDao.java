package com.meysamasadian.treasury.dao;

import com.meysamasadian.treasury.model.Account;
import com.meysamasadian.treasury.model.Document;

/**
 * Created by rahnema on 9/6/2017.
 */
public interface DocumentDao extends GenericDao<Document> {
    Document loadLastOfDocument(String pan);
}
