package com.sadatmalik.fusionweb.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumerates the permitted account types that can be associated with a user's
 * bank Account.
 *
 * @author sadatmalik
 */
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

    /**
     * Utility method that enables the quick mapping of database stored numerical
     * ids that correspond to an AccountType.
     *
     * @param id database numeric id for the account type
     */
    public static AccountType from(int id) {
        return accountTypeById.get(id);
    }
}
