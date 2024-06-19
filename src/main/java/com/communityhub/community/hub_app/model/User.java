package com.communityhub.community.hub_app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;

import static jakarta.persistence.GenerationType.IDENTITY;
import static java.util.Collections.singletonList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
   @Id
   @GeneratedValue(strategy = IDENTITY)
    private Long userId;
   @NotBlank(message = "username is required")
    private String username;
   @NotBlank(message = "Password is required")
    private String password;
   @Email
   @NotEmpty(message = "Email is required")
    private String email;
    private Instant created;
    private boolean enabled;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return singletonList(new SimpleGrantedAuthority("USER"));
    }
}
