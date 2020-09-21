package com.yeepay.base.koala.exception;


public class CommandNotFoundException extends RuntimeException{

    public CommandNotFoundException(String message) {
        super(message);
    }

}
