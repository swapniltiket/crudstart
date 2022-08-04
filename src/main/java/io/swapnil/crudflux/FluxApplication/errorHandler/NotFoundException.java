package io.swapnil.crudflux.FluxApplication.errorHandler;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

}

