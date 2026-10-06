package org.ftm.api.user;

public class UserMapper {

    public static UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId().toString())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .user_type(user.getUserType().toString())
                .enabled(user.isEnabled())
                .build();
    }

    public static User mapToCreateRequest(UserRequest request) {
        return User.builder()
                .email(request.email())
                .password(request.confirm_password())
                .accountNonExpired(true)
                .accountNonLocked(true)
                .enabled(false)
                .build();
    }
}