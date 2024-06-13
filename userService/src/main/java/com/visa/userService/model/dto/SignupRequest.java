package com.visa.userService.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {

    @Schema(description = "userName", example = "idelcas")
    @NotBlank(message = "userName cannot be blank")
    private String userName;

    @Schema(description = "password", example = "123456")
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    private String password;

    @Schema(description = "roles", example = "[\"admin\"]")
    @NotEmpty(message = "role cannot be blank")
    private Set<String> roles;

    @Schema(description = "person",type = "object")
    @NotNull(message = "person cannot be blank")
    private PersonDto person;

}
