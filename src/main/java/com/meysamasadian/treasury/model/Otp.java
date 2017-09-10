package com.meysamasadian.treasury.model;

import javax.persistence.*;

/**
 * Created by rahnema on 9/6/2017.
 */
@Entity
@Table(name = "t_otp")
public class Otp {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "c_opt")
    private String opt;

    @Column(name = "c_account")
    private Account account;

    @Column(name = "c_expiredate")
    private String expireDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOpt() {
        return opt;
    }

    public void setOpt(String opt) {
        this.opt = opt;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }
}
