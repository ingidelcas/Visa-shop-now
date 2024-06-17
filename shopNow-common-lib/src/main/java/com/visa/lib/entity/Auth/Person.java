package com.visa.lib.entity.Auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.NaturalId;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="person", schema = "shopnow",uniqueConstraints = {
        @UniqueConstraint(name = "unique_email", columnNames = "email")})
public class Person {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer personId;

    @NotBlank(message = "DNI must not be blank")
    @Column(name = "dni")
    private String dni;

    @NotBlank(message = "Full name must not be blank")
    @Size(min = 3, max = 100, message = "Full name must be between 3 and 100 characters")
    @Column(name = "fullname")
    private String fullName;

    @NotBlank(message = "Gender must not be blank")
    @Column(name = "gender", nullable = false)
    private String gender;

    @Pattern(regexp = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$", message = "The phone number is not in the correct format")
    @Size(min = 10, max = 20, message = "Phone number must be between 10 and 11 characters")
    @Column(name = "phone", unique = true)
    private String phone;

    @NaturalId
    @NotBlank
    @Size(max = 50)
    @Email(message = "Input must be in Email format")
    @Column(name = "email")
    private String email;

    @OneToOne(mappedBy = "person")
    @JsonIgnore
    private UserAccount user;

}
