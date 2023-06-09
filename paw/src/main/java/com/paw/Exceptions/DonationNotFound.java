package com.paw.Exceptions;

public class DonationNotFound extends RuntimeException{
    public DonationNotFound(String message)
    {
        super(message);
    }
}
