package com.transaction.service.models;

import com.transaction.service.enums.DebitCredit;

public class Amount {
    
    private String amount;

    private String currency;

    private DebitCredit debitOrCredit;

    public Amount(String amount, String currency, DebitCredit debitOrCredit) {
        setAmount(amount);
        setCurrency(currency);
        this.debitOrCredit = debitOrCredit;
    }

    public String getAmount() {
        return this.amount;
    }

    public void setAmount(String amount) {
        String amountString = amount.trim();
        if (amountString.length() == 0) {
            throw new IllegalArgumentException("Amount needs to have length greater than 0");
        }

        double amountDouble = Double.valueOf(amountString);

        if (amountDouble < 0) {
            throw new IllegalArgumentException("Amount needs to be greater than 0");
        }

        // decided to round to two decimal points for ease
        // can straight out reject amounts that have more than 2 decimal points to avoid rounding issues
        // at the cost of being more strict with requests
        double roundedTwoDecimals = Math.round(amountDouble * 100.0) / 100.0;

        this.amount = String.valueOf(roundedTwoDecimals);
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String currency) {
        if (currency.trim().length() == 0) {
            throw new IllegalArgumentException("Currency needs to have length greater than 0");
        }

        this.currency = currency;
    }

    public DebitCredit getDebitOrCredit() {
        return this.debitOrCredit;
    }

    public void setDebitOrCredit(DebitCredit debitOrCredit) {
        this.debitOrCredit = debitOrCredit;
    }

    @Override
    public String toString() {
        return "{" +
            " amount='" + getAmount() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", debitOrCredit='" + getDebitOrCredit() + "'" +
            "}";
    }

}
