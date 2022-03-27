package com.sadatmalik.fusionweb.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumerates the permitted expense types that can be associated with a
 * single user expense.
 *
 * @author sadatmalik
 */
public enum ExpenseType {
    BILL(1),
    GROCERIES(2),
    TRAVEL(3),
    UTILITIES(4),
    CAR(5),
    CHARITY(6);

    private final int typeId;
    private static final Map<Integer, ExpenseType> expenseTypeById;

    static {
        expenseTypeById = new HashMap<>();
        for (ExpenseType expenseType : ExpenseType.values()) {
            expenseTypeById.put(expenseType.typeId, expenseType);
        }
    }

    ExpenseType(int id) {
        this.typeId = id;
    }

    /**
     * Utility method that enables the quick mapping of database stored numerical
     * ids to the corresponding ExpenseType.
     *
     * @param id database numeric identifier for the expense type.
     */
    public static ExpenseType from(int id) {
        return expenseTypeById.get(id);
    }
}