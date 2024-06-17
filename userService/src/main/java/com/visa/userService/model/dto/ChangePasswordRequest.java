package com.visa.userService.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;


public record ChangePasswordRequest(
        @Schema(description = "old password", example = "123456")
        @NotBlank(message = "oldPassword cannot be blank")
        String oldPassword,
        @Schema(description = "new password", example = "1234567")
        @NotBlank(message = "newPassword cannot be blank")
        String newPassword,

        @Schema(description = "confirm new password", example = "1234567")
        @NotBlank(message = "confirmPassword cannot be blank")
        String confirmPassword
) {

}