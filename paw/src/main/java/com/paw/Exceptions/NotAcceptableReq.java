package com.paw.Exceptions;

public class NotAcceptableReq extends RuntimeException{
    public NotAcceptableReq(String message)
    {
        super(message);
    }
}
