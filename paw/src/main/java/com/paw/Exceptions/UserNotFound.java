package com.paw.Exceptions;

public class UserNotFound extends RuntimeException{
    public UserNotFound()
    {
        super();
    }

    public UserNotFound(String message)
    {
        super(message);
    }
}
