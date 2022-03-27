package com.sadatmalik.fusionweb.exceptions;

/**
 * Exception intended for use when the downstream lookup of weekly expense
 * data does not return any values.
 *
 * @author sadatmalik
 */
public class NoSuchWeeklyExpenseException extends Throwable {

    /**
     * Intended usage is to always provide an informational message when the
     * exception is constructed. Thus, only this parameterised constructor
     * is exposed.
     *
     * @param message exception message.
     */
    public NoSuchWeeklyExpenseException(String message) {
        super(message);
    }

}
