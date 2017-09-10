package com.meysamasadian.treasury.model;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by rahnema on 9/6/2017.
 */
@Entity
@Table(name = "t_account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "c_amount")
    private BigDecimal balance;


    @Column(name = "c_pan")
    private String pan;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }
}
