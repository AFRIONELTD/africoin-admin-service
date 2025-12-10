package com.afrione.africoinservice.infrastructure.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;


@Data
public class AuthenticatedUser implements UserDetails {

    private Long accountId;
    private Long userId;
    private String username;
    private String name;
    private String password;
    private String email;
    private Boolean enabled = true;
    private Boolean blocked = false;
    private Boolean deactivated = false;
    private Boolean expiredCredential = false;
    private boolean active = true;
    private Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return active;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    public void addAuthority(String name) {
        authorities.add(new SimpleGrantedAuthority(name));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
