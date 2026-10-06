package org.ftm.api.user;

import lombok.RequiredArgsConstructor;
import org.ftm.api.board_member.BoardMember;
import org.ftm.api.email.EmailService;
import org.ftm.api.employee.Employee;
import org.ftm.api.partner_contact.PartnerContact;
import org.ftm.api.role.Role;
import org.ftm.api.role.RoleRepository;
import org.ftm.api.security.PasswordGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAccountService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordGenerator passwordGenerator;
    private final EmailService emailService;


    @Transactional
    public User createEmployeeAccount(
            String email,
            String firstname,
            String lastname
    ) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Email déjà utilisé."
            );
        }

        String temporaryPassword = passwordGenerator.generate();

        Role role = roleRepository
                .findByName("ROLE_EMPLOYEE")
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Role EMPLOYEE introuvable."
                        )
                );

        User user = User.builder()
                .email(email)
                .password(
                        passwordEncoder.encode(
                                temporaryPassword
                        )
                )
                .firstname(firstname)
                .lastname(lastname)
                .userType(UserType.EMPLOYEE)
                .enabled(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .build();

        user.addRole(role);

        User savedUser = userRepository.save(user);

        emailService.sendWelcomeEmail(
                email,
                firstname,
                lastname,
                temporaryPassword
        );

        return savedUser;
    }

    @Transactional
    public User createBoardMemberAccount(
            String email,
            String firstname,
            String lastname
    ) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Email déjà utilisé."
            );
        }

        String temporaryPassword = passwordGenerator.generate();

        Role role = roleRepository
                .findByName("ROLE_BOARD_MEMBER")
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Role BOARD MEMBER introuvable."
                        )
                );

        User user = User.builder()
                .email(email)
                .password(
                        passwordEncoder.encode(
                                temporaryPassword
                        )
                )
                .firstname(firstname)
                .lastname(lastname)
                .userType(UserType.BOARD_MEMBER)
                .enabled(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .build();

        user.addRole(role);

        User savedUser = userRepository.save(user);

        emailService.sendWelcomeEmail(
                email,
                firstname,
                lastname,
                temporaryPassword
        );

        return savedUser;
    }

    @Transactional
    public User createPartnerContactAccount(
            String email,
            String firstname,
            String lastname
    ) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Email déjà utilisé."
            );
        }

        String temporaryPassword = passwordGenerator.generate();

        Role role = roleRepository
                .findByName("ROLE_PARTNER_CONTACT")
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Role PARTNER CONTACT introuvable."
                        )
                );

        User user = User.builder()
                .email(email)
                .password(
                        passwordEncoder.encode(
                                temporaryPassword
                        )
                )
                .firstname(firstname)
                .lastname(lastname)
                .userType(UserType.PARTNER_CONTACT)
                .enabled(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .build();

        user.addRole(role);

        User savedUser = userRepository.save(user);

        emailService.sendWelcomeEmail(
                email,
                firstname,
                lastname,
                temporaryPassword
        );

        return savedUser;
    }

    @Transactional
    public User createAdminAccount(
            String email,
            String firstname,
            String lastname
    ) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Email déjà utilisé."
            );
        }

        String temporaryPassword = passwordGenerator.generate();

        Role role = roleRepository
                .findByName("ROLE_ADMIN")
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Role ADMIN introuvable."
                        )
                );

        User user = User.builder()
                .email(email)
                .password(
                        passwordEncoder.encode(
                                temporaryPassword
                        )
                )
                .firstname(firstname)
                .lastname(lastname)
                .userType(UserType.ADMIN)
                .enabled(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .build();

        user.addRole(role);

        User savedUser = userRepository.save(user);

        emailService.sendWelcomeEmail(
                email,
                firstname,
                lastname,
                temporaryPassword
        );

        return savedUser;
    }

    @Transactional
    public User createUserAccount(
            String email,
            String firstname,
            String lastname
    ) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Email déjà utilisé."
            );
        }

        String temporaryPassword = passwordGenerator.generate();

        Role role = roleRepository
                .findByName("ROLE_USER")
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Role USER introuvable."
                        )
                );

        User user = User.builder()
                .email(email)
                .password(
                        passwordEncoder.encode(
                                temporaryPassword
                        )
                )
                .firstname(firstname)
                .lastname(lastname)
                .userType(UserType.ADMIN)
                .enabled(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .build();

        user.addRole(role);

        User savedUser = userRepository.save(user);

        emailService.sendWelcomeEmail(
                email,
                firstname,
                lastname,
                temporaryPassword
        );

        return savedUser;
    }
}
