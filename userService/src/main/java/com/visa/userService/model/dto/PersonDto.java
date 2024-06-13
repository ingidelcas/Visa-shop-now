package com.visa.userService.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {
    @Schema(description = "DNI", example = "12345")
    @NotBlank(message = "DNI cannot be blank")
    private String dni;

    @Schema(description = "fullName", example = "idelson Castano")
    @NotBlank(message = "FullName cannot be blank")
    private String fullName;

    @Schema(description = "gender", example = "male")
    @NotBlank(message = "Gender cannot be blank")
    private String gender;

    @Schema(description = "phone", example = "+57 (314) 374 2593")
    @NotBlank(message = "Phone cannot be blank")
    @Email(message = "Invalid username format")
    private String phone;

    @Schema(description = "username", example = "idelcas@hotmail.com")
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid username format")
    private String email;
}