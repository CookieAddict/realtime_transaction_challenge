package com.transaction.service.models.requests;

import com.transaction.service.models.Amount;

public class LoadRequest {

    private String userId;

    private String messageId;

    private Amount transactionAmount;

    public LoadRequest(String userId, String messageId, Amount transactionAmount) {
        setUserId(userId);
        setMessageId(messageId);
        this.transactionAmount = transactionAmount;
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

    public Amount getTransactionAmount() {
        return this.transactionAmount;
    }

    public void setTransactionAmount(Amount transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    @Override
    public String toString() {
        return "{" +
            " userId='" + getUserId() + "'" +
            ", messageId='" + getMessageId() + "'" +
            ", transactionAmount='" + getTransactionAmount() + "'" +
            "}";
    }
    
}
