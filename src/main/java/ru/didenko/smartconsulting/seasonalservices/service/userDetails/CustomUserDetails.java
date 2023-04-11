package ru.didenko.smartconsulting.seasonalservices.service.userDetails;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Class implements the UserDetails interface and contains the main information of User for authentication
 */
public class CustomUserDetails implements UserDetails {

    private final Collection<? extends GrantedAuthority> authorities;
    private final String password;
    private final String username;
    private final Long id;
    private final Boolean enabled;
    private final Boolean accountNotExpired;
    private final Boolean accountNotLocked;
    private final Boolean credentialsNotExpired;

    public CustomUserDetails(final Long id,
                             final String username,
                             final String password,
                             final Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.accountNotExpired = true;
        this.accountNotLocked = true;
        this.credentialsNotExpired = true;
        this.enabled = true;
        this.authorities = authorities;

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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
        return accountNotExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNotLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNotExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Long getUserId() {
        return id;
    }

    @Override
    public String toString() {
        return "{\"user_id\":\"" + id + "\"," +
                "\"username\":\"" + username + "\"," +
                "\"user_role\":\"" + authorities + "\"," +
                "\"user_password\":\"" + password + "\"}";
    }
}
