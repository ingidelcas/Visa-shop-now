package com.visa.userService.model.dto;

import com.visa.lib.entity.Order;
import com.visa.lib.entity.auth.Person;
import com.visa.lib.entity.auth.Role;
import com.visa.lib.entity.auth.RoleName;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountDto {
    private Integer userId;
    private String userName;
}
