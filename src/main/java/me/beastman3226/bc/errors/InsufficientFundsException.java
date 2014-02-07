/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.beastman3226.bc.errors;

/**
 *
 * @author Nicholas
 */
public class InsufficientFundsException extends Exception {

    /**
     * Creates a new instance of
     * <code>InsufficientFundsException</code> without detail message.
     */
    public InsufficientFundsException() {
    }

    /**
     * Constructs an instance of
     * <code>InsufficientFundsException</code> with the specified detail
     * message.
     *
     * @param msg the detail message.
     */
    public InsufficientFundsException(String msg) {
        super(msg);
    }
}
