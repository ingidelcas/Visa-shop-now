package com.visa.lib.entity.Auth;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "login_attempt", schema = "shopnow")
public class LoginAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "failed_attempt")
    private Integer failedAttempt;

    @Column(name = "account_non_locked")
    private Boolean accountNonLocked;

    @Column(name = "lock_time")
    private Date lockTime;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private UserAccount user;

}
