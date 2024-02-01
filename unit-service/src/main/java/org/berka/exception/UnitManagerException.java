package org.berka.exception;

import lombok.Getter;

@Getter
public class UnitManagerException extends RuntimeException{

    private final ErrorType errorType;

    public UnitManagerException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    public UnitManagerException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }
}
