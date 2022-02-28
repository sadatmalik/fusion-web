package com.sadatmalik.fusionweb.model;

import java.util.HashMap;
import java.util.Map;

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

    public static ExpenseType from(int id) {
        return expenseTypeById.get(id);
    }
}