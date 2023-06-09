package com.paw.Exceptions;

public class RewardNotFound extends RuntimeException{
    public RewardNotFound(String message)
    {
        super(message);
    }
}
