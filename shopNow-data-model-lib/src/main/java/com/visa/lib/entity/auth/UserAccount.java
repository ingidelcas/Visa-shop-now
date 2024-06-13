package com.visa.lib.entity.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.visa.lib.entity.Order;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serial;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "UserAccount", schema = "shopnow", uniqueConstraints = {
        @UniqueConstraint(name = "unique_username", columnNames = "user_name")
})
public class UserAccount {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @NotBlank(message = "Username must not be blank")
    @Size(min = 3, max = 100, message = "Username must be between 3 and 100 characters")
    @Column(name = "user_name")
    private String userName;

    @JsonIgnore
    @NotNull(message = "Password must not be null")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    @Column(name = "password")
    private String password;

    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", schema = "shopnow",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id", referencedColumnName = "personId")
    private Person person;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Order> orders;

}
