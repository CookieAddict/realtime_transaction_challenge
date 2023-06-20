package com.transaction.service.models.responses;

import com.transaction.service.enums.ResponseCode;
import com.transaction.service.models.Amount;

public class AuthorizationResponse extends LoadResponse {

    private ResponseCode responseCode;

    public AuthorizationResponse(String userId, String messageId, Amount balance, ResponseCode responseCode) {
        super(userId, messageId, balance);
        this.responseCode = responseCode;
    }

    public ResponseCode getResponseCode() {
        return this.responseCode;
    }

    public void setResponseCode(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }
    
}
