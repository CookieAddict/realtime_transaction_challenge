package com.transaction.service.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.transaction.service.managers.TransactionManager;
import com.transaction.service.models.requests.AuthorizationRequest;
import com.transaction.service.models.requests.LoadRequest;
import com.transaction.service.models.responses.AuthorizationResponse;
import com.transaction.service.models.responses.LoadResponse;
import com.transaction.service.models.responses.ServerError;

@RestController
public class TransactionController {
    
    @Autowired
    private TransactionManager transactionManager;

    private static Logger log = LogManager.getLogger(TransactionController.class);

    @PutMapping("/load/{messageId}")
    @ResponseStatus(HttpStatus.CREATED)
    public LoadResponse load(@RequestBody LoadRequest request, @PathVariable String messageId) throws ServerError {
        log.info("Received load request with message Id {}", messageId);

        
        LoadResponse response;
        try {
            // removing responseCode field from response object to comply with service spec
            response = new LoadResponse(transactionManager.processTransaction(request));
        } catch (Exception e) {
            log.error("Exception thrown during processing message Id {}",messageId, e);
            throw new ServerError("500");
        }

        return response;
    }

    @PutMapping("/authorization/{messageId}")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorizationResponse authorization(@RequestBody AuthorizationRequest request,
        @PathVariable String messageId) throws ServerError {
        log.info("Received authorization request with message Id {}", messageId);

        AuthorizationResponse response;
        try {
            response = (AuthorizationResponse) transactionManager.processTransaction(request);
        } catch (Exception e) {
            log.error("Exception thrown during processing message Id {}", messageId, e);
            throw new ServerError("500");
        }

        return response;
    }
}
