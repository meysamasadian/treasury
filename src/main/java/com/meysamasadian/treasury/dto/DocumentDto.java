package com.meysamasadian.treasury.dto;

import java.math.BigDecimal;

/**
 * Created by rahnema on 9/6/2017.
 */
public class DocumentDto {
    private long id;
    private BigDecimal amount;
    private AccountDto source;
    private AccountDto dest;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public AccountDto getSource() {
        return source;
    }

    public void setSource(AccountDto source) {
        this.source = source;
    }

    public AccountDto getDest() {
        return dest;
    }

    public void setDest(AccountDto dest) {
        this.dest = dest;
    }
}
