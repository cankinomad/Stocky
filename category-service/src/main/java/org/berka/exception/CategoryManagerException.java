package org.berka.exception;

import lombok.Getter;

@Getter
public class CategoryManagerException extends RuntimeException{

    private final ErrorType errorType;

    public CategoryManagerException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    public CategoryManagerException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }
}
