package bullish.store.controller;

import bullish.store.assembler.ProductModelAssembler;
import bullish.store.assembler.StockModelAssembler;
import bullish.store.communication.auth.AuthRegisterRequest;
import bullish.store.communication.auth.AuthenticateRequest;
import bullish.store.communication.auth.AuthenticateResponse;
import bullish.store.configuration.ApplicationConfig;
import bullish.store.configuration.SecurityConfig;
import bullish.store.configuration.jwt.JwtAuthFilter;
import bullish.store.configuration.jwt.JwtService;
import bullish.store.repository.UserRepository;
import bullish.store.service.auth.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import({JwtAuthFilter.class, JwtService.class, ProductModelAssembler.class, StockModelAssembler.class, SecurityConfig.class, ApplicationConfig.class})
class AuthControllerSecurityTest {

    @MockBean
    private AuthService authService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithAnonymousUser
    void AuthenticateShouldBeAvailableToAll() throws Exception {
        when(authService.authenticate(any(AuthenticateRequest.class)))
                .thenReturn(AuthenticateResponse.builder().jwtToken("jwt-token").build());

        mockMvc.perform(post("/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"test\",\"password\":\"test\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void RegisterShouldBeAvailableToAll() throws Exception {
        when(authService.register(any(AuthRegisterRequest.class)))
                .thenReturn(AuthenticateResponse.builder().jwtToken("jwt-token").build());
        AuthRegisterRequest request = AuthRegisterRequest.builder()
                .username("user").password("user").build();

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }
}