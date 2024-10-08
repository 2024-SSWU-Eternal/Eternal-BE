package com.example.eternal.service.user;

import com.example.eternal.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Implement logic to return authorities (e.g., roles)
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // Customize based on your business logic
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // Customize based on your business logic
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // Customize based on your business logic
    }

    @Override
    public boolean isEnabled() {
        return true;  // Customize based on your business logic
    }
}
