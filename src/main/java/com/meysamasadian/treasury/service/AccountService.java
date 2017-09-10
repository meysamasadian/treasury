package com.meysamasadian.treasury.service;

import com.meysamasadian.treasury.dao.AccountDao;
import com.meysamasadian.treasury.dto.AccountDto;
import com.meysamasadian.treasury.dto.OtpContainer;
import com.meysamasadian.treasury.exception.BusinessException;
import com.meysamasadian.treasury.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;

/**
 * Created by rahnema on 9/6/2017.
 */
@Service
@PropertySource(value = {"classpath:treasury.properties"})
public class AccountService {
    @Autowired
    private AccountDao accountDao;

    @Autowired
    private Cache otpCache;

    @Autowired
    private Environment environment;

    public void validate(Account account, String otp) throws BusinessException {
        Cache.ValueWrapper wrapper = otpCache.get(account.getPan());

        OtpContainer container =  wrapper != null ? (OtpContainer)wrapper.get() : null;
        if (container != null) {
            if (!container.getCode().equals(otp)) {
                throw new BusinessException(BusinessException.OTP_IS_NOT_VALID);
            }
        } else {
            throw new BusinessException(BusinessException.OTP_IS_NOT_VALID);
        }
    }

    public String login(String pan) {
        Cache.ValueWrapper wrapper = otpCache.get(pan);

        OtpContainer container =  wrapper != null ? (OtpContainer)wrapper.get() : null;
        if (container != null) {
            if (container.getExpire() > System.currentTimeMillis()) {
                return container.getCode();
            }
        }
        otpCache.put(pan,generateOtp());
        return ((OtpContainer) otpCache.get(pan).get()).getCode();
    }


    private OtpContainer generateOtp() {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        int i = 0;
        while(i < Integer.parseInt(environment.getRequiredProperty("otp.expire_length"))) {
            builder.append(random.nextInt(9));
            i++;
        }
        return new OtpContainer(builder.toString(),
                System.currentTimeMillis() + Long.parseLong(environment.getRequiredProperty("otp.expire_duration")));
    }

    public void register(AccountDto dto) {
        accountDao.save(convert(dto));
    }

    public AccountDto load(String pan) {
        return present(accountDao.load(pan));
    }


    public void increaseBalance(AccountDto dto, BigDecimal amount) {
        Account account = accountDao.load(dto.getPan());
        account.setBalance(account.getBalance().add(amount));
        accountDao.update(account);
    }

    public void decreaseBalance(AccountDto dto, BigDecimal amount) {
        Account account = accountDao.load(dto.getPan());
        account.setBalance(account.getBalance().subtract(amount));
        accountDao.update(account);
    }

    public Account convert(AccountDto accountDto) {
        Account account = new Account();
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
