package com.visa.lib.entity.auth;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "login_attempt", schema = "shopnow")
public class LoginAttempt {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Username must not be blank")
    @Size(min = 3, max = 100, message = "Username must be between 3 and 100 characters")
    @Column(name = "username")
    String username;

    @NotNull(message = "success must not be blank")
    @Column(name = "success")
    boolean success;

    @NotNull(message = "createdAt must not be blank")
    @Column(name = "created_at")
    LocalDateTime createdAt;

}
