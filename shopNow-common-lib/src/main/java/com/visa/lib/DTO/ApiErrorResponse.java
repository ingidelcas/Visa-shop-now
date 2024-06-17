package com.visa.lib.DTO;


public record ApiErrorResponse(
        int errorCode,
        String description) {

}
