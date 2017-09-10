package com.meysamasadian.treasury.dao;

import com.meysamasadian.treasury.model.Account;
import com.meysamasadian.treasury.model.Document;
import com.meysamasadian.treasury.model.Otp;

/**
 * Created by rahnema on 9/6/2017.
 */
public interface AccountDao extends GenericDao<Account> {
    Account load(String pan);
    void login(Otp otp);
    void logout(Otp otp);
    Otp getOpt(String pan);
}
