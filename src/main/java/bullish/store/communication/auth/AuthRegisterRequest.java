package bullish.store.communication.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public @Data class AuthRegisterRequest {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
}
