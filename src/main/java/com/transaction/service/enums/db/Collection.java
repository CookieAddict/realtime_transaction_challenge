package com.transaction.service.enums.db;

public enum Collection {
    ACCOUNTS("accounts");

    String name;

    private Collection(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
