package org.ftm.api.security;

import lombok.RequiredArgsConstructor;
import org.ftm.api.user.User;
import org.ftm.api.user.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(@NonNull String userEmail) throws UsernameNotFoundException {
        User user = userRepository.findByEmailWithAuthorities(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with id: " + userEmail));
        return new CustomUserDetails(user);
    }
}
