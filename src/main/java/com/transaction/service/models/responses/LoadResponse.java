package com.transaction.service.models.responses;

import com.transaction.service.models.Amount;
import com.transaction.service.models.requests.LoadRequest;

public class LoadResponse {

    private String userId;

    private String messageId;

    private Amount balance;

    public LoadResponse(String userId, String messageId, Amount balance) {
        setUserId(userId);
        setMessageId(messageId);
        this.balance = balance;
    }

    public LoadResponse(LoadRequest request, Amount balance) {
        setUserId(request.getUserId());
        setMessageId(request.getMessageId());
        this.balance = balance;
    }

    public LoadResponse(LoadResponse response) {
        this.userId = response.userId;
        this.messageId = response.messageId;
        this.balance = response.balance;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        if (userId.trim().length() == 0) {
            throw new IllegalArgumentException("User Id needs to have length greater than 0");
        }

        this.userId = userId;
    }

    public String getMessageId() {
        return this.messageId;
    }

    public void setMessageId(String messageId) {
        if (messageId.trim().length() == 0) {
            throw new IllegalArgumentException("Message Id needs to have length greater than 0");
        }

        this.messageId = messageId;
    }

    public Amount getBalance() {
        return this.balance;
    }

    public void setBalance(Amount balance) {
        this.balance = balance;
    }

}
