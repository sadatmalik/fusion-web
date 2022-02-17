package com.sadatmalik.fusionweb.model;

import java.util.HashMap;
import java.util.Map;

public enum AccountType {
    CURRENT(1),
    SAVINGS(2),
    CASH(3);

    private final int typeId;
    private static final Map<Integer, AccountType> accountTypeById;

    static {
        accountTypeById = new HashMap<>();
        for (AccountType accountType : AccountType.values()) {
            accountTypeById.put(accountType.typeId, accountType);
        }
    }

    AccountType(int id) {
        this.typeId = id;
    }

    public static AccountType from(int id) {
        return accountTypeById.get(id);
    }
}
