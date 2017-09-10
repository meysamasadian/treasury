package com.meysamasadian.treasury.controller;

import com.meysamasadian.treasury.dto.AccountDto;
import com.meysamasadian.treasury.dto.DocumentContainerFactory;
import com.meysamasadian.treasury.dto.DocumentDto;
import com.meysamasadian.treasury.exception.BusinessException;
import com.meysamasadian.treasury.model.Account;
import com.meysamasadian.treasury.service.AccountService;
import com.meysamasadian.treasury.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;

/**
 * Created by rahnema on 9/6/2017.
 */
@RestController
@RequestMapping("/document")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/issue/{opt}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity issueDocument(@RequestBody DocumentDto documentDto, @PathVariable String opt) {
        try {
            AccountDto source = accountService.load(documentDto.getSource());
            accountService.validate(accountService.convert(source), opt);
            String refId = documentService.issueDocument(documentDto);
            String message = String.valueOf(documentDto.getAmount()) + " was transfer from "
                    + documentDto.getSource() + " to " + documentDto.getDest() + " successfully!!";
            return new ResponseEntity(DocumentContainerFactory.newOkInstance(refId,message), HttpStatus.OK);
        } catch (BusinessException e) {
            e.printStackTrace();
            return new ResponseEntity(DocumentContainerFactory.newBadInstance(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/reverse/{refId}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity reverseLastDocument(@PathVariable String refId) {
        try {
            String reverseRefId = documentService.reverseDocument(refId);
            String message = "Document " + refId + " was reversed successfully!!";
            return new ResponseEntity(DocumentContainerFactory.newOkInstance(reverseRefId, message), HttpStatus.OK);
        } catch (BusinessException e) {
            e.printStackTrace();
            return new ResponseEntity(DocumentContainerFactory.newBadInstance(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
