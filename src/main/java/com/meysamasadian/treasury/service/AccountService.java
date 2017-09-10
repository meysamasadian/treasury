package com.meysamasadian.treasury.service;

import com.meysamasadian.treasury.dao.AccountDao;
import com.meysamasadian.treasury.dto.AccountDto;
import com.meysamasadian.treasury.exception.BusinessException;
import com.meysamasadian.treasury.model.Account;
import com.meysamasadian.treasury.model.Otp;
import org.joda.time.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

/**
 * Created by rahnema on 9/6/2017.
 */
@Service
public class AccountService {
    @Autowired
    private AccountDao accountDao;

    public void validate(Account account, String otp) throws BusinessException {
        Otp dbOtp = accountDao.getOpt(account.getPan());
        if (!isValid(dbOtp)) {
            throw new BusinessException(BusinessException.OTP_IS_NOT_VALID);
        }
    }

    public String login(String pan) {
        Otp otp = accountDao.getOpt(pan);
        if (isValid(otp)) {
            return otp.getOpt();
        } else {
            if (otp != null) {
                accountDao.logout(otp);
            }
        }
        return newOtp(pan);
    }

    private String newOtp(String pan) {
        Account account = accountDao.load(pan);
        Otp otp = new Otp();
        Date date = new Date();
        otp.setAccount(account);
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        localDateTime = localDateTime.plusDays(1);
        otp.setExpireDate(localDateTime.toString());
        otp.setOpt(generateOtp());
        accountDao.login(otp);
        return otp.getOpt();
    }

    private String generateOtp() {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        int i = 0;
        while(i < 4) {
            builder.append(random.nextInt(9));
        }
        return builder.toString();
    }

    private boolean isValid(Otp otp) {
        if (otp == null) {
            return false;
        }
        Date date = new Date();
        return otp.getExpireDate().compareTo(date.toString()) >= 0;
    }

    public void register(AccountDto dto) {
        accountDao.save(convert(dto));
    }

    public AccountDto load(String pan) {
        return present(accountDao.load(pan));
    }


    public BigDecimal getCurrentBalance(String pan) {
        return accountDao.load(pan).getBalance();
    }

    public void increaseBalance(AccountDto dto, BigDecimal amount) {
        dto.setBalance(dto.getBalance().subtract(amount));
    }

    public void decreaseBalance(AccountDto dto, BigDecimal amount) {
        dto.setBalance(dto.getBalance().add(amount));
    }

    public Account convert(AccountDto accountDto) {
        Account account = new Account();
        account.setId(accountDto.getId());
        account.setPan(accountDto.getPan());
        account.setBalance(accountDto.getBalance());
        return account;
    }

    public AccountDto present(Account account) {
        AccountDto accountDto = new AccountDto();
        accountDto.setId(account.getId());
        accountDto.setPan(account.getPan());
        accountDto.setBalance(account.getBalance());
        return accountDto;
    }
}
