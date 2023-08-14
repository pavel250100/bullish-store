package bullish.store.communication.user;

import bullish.store.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public @Data class User {

    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Set<String> roles;

    public static User toDto(UserEntity userEntity) {
        return User.builder()
                .username(userEntity.getUsername())
                .email(userEntity.getDetails().getEmail())
                .firstName(userEntity.getDetails().getFirstName())
                .lastName(userEntity.getDetails().getLastName())
                .roles(userEntity.getRoles()
                        .stream().map(userRoleEntity ->
                                userRoleEntity.getRole().toString()
                        ).collect(Collectors.toSet()))
                .build();
    }

    public static List<User> toDtoList(List<UserEntity> userEntities) {
        return userEntities.stream()
                .map(User::toDto)
                .collect(Collectors.toList());
    }
}
