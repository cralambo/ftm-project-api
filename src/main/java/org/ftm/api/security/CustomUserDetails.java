package org.ftm.api.security;

import lombok.Getter;
import org.ftm.api.permission.RolePermission;
import org.ftm.api.role.UserRole;
import org.ftm.api.user.User;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class CustomUserDetails implements UserDetails {

    private final UUID id;
    private final String email;
    private final String password;
    private final String firstname;
    private final String lastname;
    private final String fullName;
    private final String userType;
    private final boolean enabled;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;

    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.firstname = user.getFirstname() != null ? user.getFirstname() : null;
        this.lastname = user.getLastname() != null ? user.getLastname(): null;
        this.userType = user.getUserType() != null ? user.getUserType().name() : null;
        this.fullName = user.getFirstname() + " " + user.getLastname();
        this.enabled = user.isEnabled();
        this.accountNonExpired = user.isAccountNonExpired();
        this.accountNonLocked = user.isAccountNonLocked();
        this.credentialsNonExpired = user.isCredentialsNonExpired();
        this.authorities = buildAuthorities(user);
    }

    private Collection<? extends GrantedAuthority> buildAuthorities(User user) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (UserRole userRole : user.getUserRoles()) {
            if (userRole.getRole() == null) {
                continue;
            }
            authorities.add(
                    new SimpleGrantedAuthority( userRole.getRole().getName())
            );
            for (RolePermission rolePermission : userRole.getRole().getRolePermissions()) {
                if (rolePermission.getPermission() == null) {
                    continue;
                }
                authorities.add(
                        new SimpleGrantedAuthority( rolePermission .getPermission() .getName() )
                );
            }
        }
        return authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
