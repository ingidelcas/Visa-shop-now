package com.visa.userService.model.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountDto {
    private Integer userId;
    private String userName;
}
