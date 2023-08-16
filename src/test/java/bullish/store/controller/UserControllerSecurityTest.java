package bullish.store.controller;

import bullish.store.assembler.ProductModelAssembler;
import bullish.store.assembler.StockModelAssembler;
import bullish.store.assembler.UserModelAssembler;
import bullish.store.configuration.ApplicationConfig;
import bullish.store.configuration.SecurityConfig;
import bullish.store.configuration.jwt.JwtAuthFilter;
import bullish.store.configuration.jwt.JwtService;
import bullish.store.entity.UserDetailsEntity;
import bullish.store.entity.UserEntity;
import bullish.store.repository.UserRepository;
import bullish.store.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import({JwtAuthFilter.class, JwtService.class, ProductModelAssembler.class, StockModelAssembler.class, UserModelAssembler.class, SecurityConfig.class, ApplicationConfig.class})
class UserControllerSecurityTest {

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    private UserEntity dummyUser() {
        UserDetailsEntity detailsEntity = UserDetailsEntity.builder()
                .email("test@gmail.com")
                .firstName("first-name-test")
                .build();
        return UserEntity.builder()
                .username("test-user")
                .details(detailsEntity)
                .roles(Set.of())
                .build();
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void OnlyAdminsCanGetAllUsers() throws Exception {
        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void RestrictGetAllUsersFromNobody() throws Exception {
        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "user-1")
    void RestrictGetAllUsersFromUsers() throws Exception {
        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user-1", authorities = {"USER"})
    void RestrictGetUserToCallingUserOnly() throws Exception {
        mockMvc.perform(get("/users/{}", "another-username")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user-1", authorities = {"USER"})
    void AllowGetUserToCallingUserOnly() throws Exception {
        when(userService.getByUsername("user-1")).thenReturn(dummyUser());
        mockMvc.perform(get("/users/{username}", "user-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void AllowGetUserToAdmins() throws Exception {
        when(userService.getByUsername("user-1")).thenReturn(dummyUser());
        mockMvc.perform(get("/users/{username}", "user-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}