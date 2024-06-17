package com.visa.userService.model.dto;


import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Date;

public record LoginAttemptResponse(
        @Schema(description = "The date and time of the login attempt") LocalDateTime createdAt,
        @Schema(description = "The login status") boolean success,

        @Schema(description = "Number of failed loging attempts ") Integer failedAttempt,

        @Schema(description = "The login status") Boolean accountNonLocked,

        @Schema(description = "Lock time") Date lockTime

) {

}
