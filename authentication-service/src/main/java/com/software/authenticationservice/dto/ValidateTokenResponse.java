package com.software.authenticationservice.dto;


public class ValidateTokenResponse {
    private boolean valid;

    public ValidateTokenResponse(boolean tokenValid) {
        this.valid=tokenValid;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
