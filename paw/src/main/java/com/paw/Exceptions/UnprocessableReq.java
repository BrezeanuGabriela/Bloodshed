package com.paw.Exceptions;

public class UnprocessableReq extends RuntimeException{
    public UnprocessableReq(String message)
    {
        super(message);
    }
}
