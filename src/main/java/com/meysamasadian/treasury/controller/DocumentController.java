package com.meysamasadian.treasury.controller;

import com.meysamasadian.treasury.dto.AccountDto;
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
            AccountDto source = accountService.load(documentDto.getSource().getPan());
            accountService.validate(accountService.convert(source), opt);
            documentService.issueDocument(documentDto);
            String message = String.valueOf(documentDto.getAmount()) + " was transfer from "
                    + documentDto.getSource().getPan() + " to " + documentDto.getDest().getPan() + " successfully!!";
            return new ResponseEntity(message, HttpStatus.OK);
        } catch (BusinessException e) {
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/reverse", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity reverseLastDocument(@RequestBody AccountDto accountDto, @PathVariable String opt) {
        try {
            AccountDto source = accountService.load(accountDto.getPan());
            accountService.validate(accountService.convert(source), opt);
            documentService.reverseLastDocument(accountDto);
            String message = "Document reversed successfully!!";
            return new ResponseEntity(message, HttpStatus.OK);
        } catch (BusinessException e) {
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
