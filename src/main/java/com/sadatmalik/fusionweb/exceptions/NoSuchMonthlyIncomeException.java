package com.sadatmalik.fusionweb.exceptions;

/**
 * Exception intended for use when the downstream lookup of monthly income
 * data does not return any values.
 *
 * @author sadatmalik
 */
public class NoSuchMonthlyIncomeException extends Throwable {

    /**
     * Intended usage is to always provide an informational message when the
     * exception is constructed. Thus, only this parameterised constructor
     * is exposed.
     *
     * @param message exception message.
     */
    public NoSuchMonthlyIncomeException(String message) {
        super(message);
    }

}
