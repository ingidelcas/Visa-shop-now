package com.visa.lib.entity.auth;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.visa.lib.entity.auth.RoleName;
import com.visa.lib.entity.auth.UserAccount;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.NaturalId;

import java.io.Serial;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "roles", schema = "shopnow")
public class Role {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", unique = true, nullable = false, updatable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(name = "rolename", length = 60)
    private RoleName name;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private Set<UserAccount> user = new HashSet<>();
}
