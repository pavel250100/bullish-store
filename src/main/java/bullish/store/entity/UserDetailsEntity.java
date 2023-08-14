package bullish.store.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "user_details")
@Entity(name = "user_details")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"user"})
public @Data class UserDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @OneToOne(mappedBy = "details")
    private UserEntity user;

    @Override
    public String toString() {
        return "UserDetails[" +
                "userId = '" + this.userId + "', " +
                "firstName = '" + this.firstName + "', " +
                "lastName = '" + this.lastName + "', " +
                "email = '" + this.email + "'" +
                "]";
    }

}
