package com.bestforpets.Model.Usuarios;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class Usuarios implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long idUser;
    @Column(nullable = false)
    private String names;

    @Column(nullable = false)
    private String lastName;

    private String fullName;

    @Column(nullable = false)
    private String identificationNumber;

    @Column(nullable = false)
    private String telephone;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;
    private LocalDateTime lastConnection;
    private Date date_created;
    private String code_verification;
    private LocalDateTime code_verification_expiry;
    private Boolean status;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private List<PasswordHistory> passwordHistory = new ArrayList<>();

    public void addPasswordToHistory(String oldPassword) {
        PasswordHistory history = new PasswordHistory();
        history.setPassword(oldPassword);
        history.setUsuarios(this);
        passwordHistory.add(history);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
