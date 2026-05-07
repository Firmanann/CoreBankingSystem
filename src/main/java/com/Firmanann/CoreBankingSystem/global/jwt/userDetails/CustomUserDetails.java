package com.Firmanann.CoreBankingSystem.global.jwt.userDetails;

import com.Firmanann.CoreBankingSystem.user.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final UserEntity user;

    public CustomUserDetails(UserEntity user) {
        this.user = user;
    }

    //Authorization
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(); // Kosongin dulu, nanti buat Role
    }

    public Long getId() {
        return user.getId();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // Kita pakai Email untuk login
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
