package bullish.store.service.auth;

import bullish.store.communication.auth.AuthenticateRequest;
import bullish.store.communication.auth.AuthenticateResponse;
import bullish.store.communication.auth.AuthRegisterRequest;
import bullish.store.configuration.jwt.JwtService;
import bullish.store.entity.UserDetailsEntity;
import bullish.store.entity.UserEntity;
import bullish.store.entity.UserRoleEntity;
import bullish.store.exception.user.UserAlreadyExistsException;
import bullish.store.exception.user.UserBadCredentialsException;
import bullish.store.exception.user.UserNotFoundException;
import bullish.store.repository.UserRepository;
import bullish.store.service.user.SecurityUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthenticateResponse register(AuthRegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException(request.getUsername());
        }

        UserDetailsEntity userDetails = UserDetailsEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .build();

        UserRoleEntity userRole = UserRoleEntity.builder()
                .role(UserRoleEntity.Role.USER)
                .build();

        UserEntity newUser = UserEntity.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .details(userDetails)
                .roles(Set.of(userRole))
                .build();

        userRepository.save(newUser);
        String jwtToken = jwtService.generateToken(new SecurityUserDetails(newUser));
        return AuthenticateResponse.builder().jwtToken(jwtToken).build();
    }

    @Override
    @Transactional(readOnly = true)
    public AuthenticateResponse authenticate(AuthenticateRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException ex) {
            throw new UserBadCredentialsException();
        }

        UserEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException(request.getUsername()));

        String jwtToken = jwtService.generateToken(new SecurityUserDetails(user));
        return AuthenticateResponse.builder().jwtToken(jwtToken).build();
    }
}
