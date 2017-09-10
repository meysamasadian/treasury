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

    public String issueDocument(DocumentDto dto) throws BusinessException {
        Account source = accountDao.load(dto.getSource());
        Account dest = accountDao.load(dto.getDest());
        BigDecimal amount = dto.getAmount();
        if (amount.compareTo(source.getBalance()) > 0) {
            throw new BusinessException(BusinessException.NOT_ENOUGH_MONEY);
        }

        accountService.decreaseBalance(accountService.present(source),amount);
        accountService.increaseBalance(accountService.present(dest),amount);
        Account sourceOrigin = accountDao.load(source.getPan());
        Account destOrigin = accountDao.load(dest.getPan());

        Document document = new Document();
        document.setAmount(amount);
        document.setSource(sourceOrigin);
        document.setDest(destOrigin);
        Date date = new Date();
        document.setDate(date.toString());
        document.setRefId(generateDocumentRefId());
        documentDao.save(document);
        return document.getRefId();
    }

    private String generateDocumentRefId() {
        return "DOC_"+System.currentTimeMillis();
    }

    public String reverseDocument(String refId) throws BusinessException {
        Document document = documentDao.load(refId);
        if (document != null) {
            DocumentDto documentDto = new DocumentDto();
            documentDto.setAmount(document.getAmount());
            documentDto.setSource(document.getDest().getPan());
            documentDto.setDest(document.getSource().getPan());
            return issueDocument(documentDto);
        }
        throw new BusinessException(BusinessException.DOCUMENT_NOT_EXISTS);
    }

}
