package com.paw.Exceptions;

public class UserAlreadyExists extends RuntimeException{
    public UserAlreadyExists()
    {
        super();
    }

    public UserAlreadyExists(String message)
    {
        super(message);
    }
}
