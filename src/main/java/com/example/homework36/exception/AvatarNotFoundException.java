package com.example.homework35.exception;

public class AvatarNotFoundException extends RuntimeException{

    private final long id;

    public AvatarNotFoundException(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

}
