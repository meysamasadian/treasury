package com.meysamasadian.treasury.dao;

import com.meysamasadian.treasury.model.Account;

/**
 * Created by rahnema on 9/6/2017.
 */
public interface AccountDao extends GenericDao<Account> {
    Account load(String pan);
}
