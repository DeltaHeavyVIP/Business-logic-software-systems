package com.example.blps.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "jwt_users")
public class JwtUsers implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotBlank
    @Column(name = "username")
    private String username;

    @NotBlank
    @Column(name = "password")
    private String password;

    @NotBlank
    @Column(name = "refreshToken")
    private String refreshToken;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "jwt_users_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "jwt_roles_id", referencedColumnName = "id"))
    private Set<JwtRole> roles;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    private Users user;

    public JwtUsers() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
