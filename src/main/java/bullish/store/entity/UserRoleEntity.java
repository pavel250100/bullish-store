package bullish.store.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity(name = "user_role")
@Table(name = "user_roles")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"users"})
public @Data class UserRoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @ManyToMany(mappedBy = "roles")
    private Set<UserEntity> users;

    public enum Role {
        ADMIN,
        USER
    }

    @Override
    public String toString() {
        return "UserRole[" +
                "id = '" + this.id + "', " +
                "role = '" + this.role + "'" +
                "]";
    }
}
