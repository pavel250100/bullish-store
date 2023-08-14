package bullish.store.controller;

import bullish.store.assembler.ProductModelAssembler;
import bullish.store.assembler.StockModelAssembler;
import bullish.store.assembler.UserModelAssembler;
import bullish.store.configuration.jwt.JwtAuthFilter;
import bullish.store.configuration.jwt.JwtService;
import bullish.store.entity.UserDetailsEntity;
import bullish.store.entity.UserEntity;
import bullish.store.exception.user.UserNotFoundException;
import bullish.store.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({ProductModelAssembler.class, StockModelAssembler.class, JwtAuthFilter.class, JwtService.class})
class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    static class UserControllerTestContextConfiguration {
        @Bean
        public UserModelAssembler userModelAssembler() { return new UserModelAssembler(); }
    }

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
    void ShouldReturnUserByUsername() throws Exception {
        UserEntity userEntity = dummyUser();
        when(userService.getByUsername("test-user")).thenReturn(userEntity);
        mockMvc.perform(get("/users/{username}", "test-user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", equalTo("test-user")))
                .andExpect(jsonPath("$.firstName", equalTo("first-name-test")));
    }

    @Test
    void ShouldReturnUserNotFoundExceptionMessage_AndNotFoundStatus() throws Exception {
        when(userService.getByUsername("test-user")).thenThrow(new UserNotFoundException("test-user"));
        mockMvc.perform(get("/users/{username}", "test-user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Could not find user test-user"));
    }

    @Test
    void ShouldReturnAllUsers_AndStatusOk() throws Exception {
        UserEntity userEntity1 = dummyUser();
        UserEntity userEntity2 = dummyUser().toBuilder().username("test-user-2").build();
        when(userService.getAll()).thenReturn(Arrays.asList(userEntity1, userEntity2));

        mockMvc.perform(get("/users", "test-user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.userList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.userList[0].username", equalTo("test-user")))
                .andExpect(jsonPath("$._embedded.userList[1].username", equalTo("test-user-2")));
    }

}