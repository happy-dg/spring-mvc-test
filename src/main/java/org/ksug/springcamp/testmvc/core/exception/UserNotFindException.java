package org.ksug.springcamp.testmvc.core.exception;

public class UserNotFindException extends RuntimeException{
    public UserNotFindException(String message) {
        super(message);
    }
}
