package dev.vishal.userservice.exceptions;

public class UserAlreadyExistsException extends Exception{

    public UserAlreadyExistsException(String message){
        super(message);
    }
}
