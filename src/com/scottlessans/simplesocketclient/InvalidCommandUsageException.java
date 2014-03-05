package com.scottlessans.simplesocketclient;

/**
 * User: slessans
 * Date: 3/5/14
 * Time: 1:05 PM
 */
public class InvalidCommandUsageException extends Exception {

    public InvalidCommandUsageException(String message) {
        super(message);
    }

    public InvalidCommandUsageException(String message, Throwable cause) {
        super(message, cause);
    }
}
