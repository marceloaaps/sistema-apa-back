package com.apa.back.infra.exceptions;

public class UserApprovalErrorException extends RuntimeException {
    public UserApprovalErrorException(String message) {
        super(message);
    }
}
