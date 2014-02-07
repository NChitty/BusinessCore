/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.beastman3226.bc.errors;

/**
 *
 * @author Nicholas
 */
public class NoOpenIDException extends Exception {

    /**
     * Creates a new instance of
     * <code>NoOpenIDException</code> without detail message.
     */
    public NoOpenIDException() {
        super("There is no open ID.");
    }

    /**
     * Constructs an instance of
     * <code>NoOpenIDException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public NoOpenIDException(String msg) {
        super(msg);
    }

    public NoOpenIDException(int id) {
        super(id + " is taken! Please clean businesses. Business creation failed.");
    }
}
