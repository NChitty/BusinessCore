/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.beastman3226.BusinessCore.error;

/**
 *
 * @author Nicholas
 */
public class InvalidFormatException extends Exception {

    /**
     * Creates a new instance of
     * <code>InvalidFormatException</code> without detail message.
     */
    public InvalidFormatException() {
    }

    /**
     * Constructs an instance of
     * <code>InvalidFormatException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidFormatException(String msg) {
        super(msg);
    }
}
