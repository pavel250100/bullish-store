package bullish.store.service.user;

import bullish.store.entity.UserEntity;
import bullish.store.exception.user.UserNotFoundException;
import bullish.store.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private UserServiceImpl userService;
    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void ShouldReturnUser_OnGetByUsername() throws UserNotFoundException {
        UserEntity user = UserEntity.builder().username("user").build();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        UserEntity returnedUser = userService.getByUsername("user");

        assertNotNull(returnedUser);
        assertEquals("user", returnedUser.getUsername());
    }

    @Test
    void ShouldThrowUserNotFoundException_OnGetByUsername() throws UserNotFoundException {
        when(userRepository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());
        Exception exception = assertThrows(UserNotFoundException.class, () -> userService.getByUsername("nonExistentUser"));
        assertEquals("Could not find user nonExistentUser", exception.getMessage());
    }

    @Test
    void ShouldReturnAllUsers() {
        UserEntity userEntity1 = UserEntity.builder().username("test-user-1").password("test-password").build();
        UserEntity userEntity2 = UserEntity.builder().username("test-user-2").password("test-password").build();
        List<UserEntity> availableUsers = Arrays.asList(userEntity1, userEntity2);

        when(userRepository.findAll()).thenReturn(availableUsers);

        List<UserEntity> returnedUsers = userService.getAll();

        assertEquals(returnedUsers, availableUsers);
    }

}