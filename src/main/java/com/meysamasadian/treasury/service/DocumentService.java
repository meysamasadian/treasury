package com.meysamasadian.treasury.service;

import com.meysamasadian.treasury.dao.AccountDao;
import com.meysamasadian.treasury.dao.DocumentDao;
import com.meysamasadian.treasury.dto.AccountDto;
import com.meysamasadian.treasury.dto.DocumentDto;
import com.meysamasadian.treasury.exception.BusinessException;
import com.meysamasadian.treasury.model.Account;
import com.meysamasadian.treasury.model.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by rahnema on 9/6/2017.
 */
@Service
public class DocumentService {

    @Autowired
    private AccountService accountService;

    @Autowired
    private DocumentDao documentDao;

    @Autowired
    private AccountDao accountDao;

    public void issueDocument(DocumentDto dto) throws BusinessException {
        AccountDto source = dto.getSource();
        AccountDto dest = dto.getDest();
        BigDecimal amount = dto.getAmount();
        if (amount.compareTo(source.getBalance()) < 0) {
            throw new BusinessException(BusinessException.NOT_ENOUGH_MONEY);
        }

        accountService.decreaseBalance(source,amount);
        accountService.increaseBalance(dest,amount);
        Account sourceOrigin = accountDao.load(source.getPan());
        Account destOrigin = accountDao.load(dest.getPan());

        Document document = new Document();
        document.setAmount(amount);
        document.setSource(sourceOrigin);
        document.setSource(destOrigin);
        Date date = new Date();
        document.setDate(date.toString());
        documentDao.save(document);
    }

    public void reverseLastDocument(AccountDto accountDto) throws BusinessException {
        Document document = documentDao.loadLastOfDocument(accountDto.getPan());
        if (document != null) {
            AccountDto source = accountService.present(document.getSource());
            AccountDto dest = accountService.present(document.getDest());
            BigDecimal amount = document.getAmount();
            accountService.increaseBalance(source, amount);
            accountService.decreaseBalance(dest,amount);
            documentDao.delete(document);
        }
        throw new BusinessException(BusinessException.DOCUMENT_NOT_EXISTS);
    }

}
