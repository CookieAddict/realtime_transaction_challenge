package com.transaction.service.models.requests;

import com.transaction.service.models.Amount;

public class AuthorizationRequest extends LoadRequest {

    public AuthorizationRequest(String userId, String messageId, Amount transactionAmount) {
        super(userId, messageId, transactionAmount);
    }

}
