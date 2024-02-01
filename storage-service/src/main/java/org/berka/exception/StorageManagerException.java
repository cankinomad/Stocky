package org.berka.exception;

import lombok.Getter;

@Getter
public class StorageManagerException extends RuntimeException{

    private final ErrorType errorType;

    public StorageManagerException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    public StorageManagerException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }
}
