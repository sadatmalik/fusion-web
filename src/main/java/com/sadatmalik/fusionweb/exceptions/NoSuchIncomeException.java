package com.sadatmalik.fusionweb.exceptions;

/**
 * Exception intended for use when the downstream lookup of income data does
 * not return any values.
 *
 * @author sadatmalik
 */
public class NoSuchIncomeException extends Throwable {

    /**
     * Intended usage is to always provide an informational message when the
     * exception is constructed. Therefore only this parameterised constructor
     * is exposed.
     *
     * @param message exception message.
     */
    public NoSuchIncomeException(String message) {
        super(message);
    }

}
