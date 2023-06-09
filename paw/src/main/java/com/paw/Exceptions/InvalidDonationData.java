package com.paw.Exceptions;

public class InvalidDonationData extends RuntimeException{
    public InvalidDonationData(String message)
    {
        super(message);
    }
}
