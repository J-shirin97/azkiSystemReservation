package test;

import org.azkiTest.DTOs.LoginRequest;
import org.azkiTest.DTOs.RegisterRequest;
import org.azkiTest.config.JwtTokenUtil;
import org.azkiTest.model.Users;
import org.azkiTest.repository.UsersRepository;
import org.azkiTest.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private UserService userService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private Users user;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest("username", "user@example.com", "password123");
        loginRequest = new LoginRequest("username", "password123");
        user = new Users();
        user.setUsername("username");
        user.setEmail("user@example.com");
        user.setPassword("encodedpassword123");
    }

    @Test
    void testRegisterUser_Success() {
        Mockito.when(usersRepository.findByUsername(registerRequest.getUsername()))
                .thenReturn(Optional.empty());

        Mockito.when(passwordEncoder.encode(registerRequest.getPassword()))
                .thenReturn("encodedpassword123");
        userService.registerUser(registerRequest);
        Mockito.verify(usersRepository, Mockito.times(1)).save(Mockito.any(Users.class));
    }

    @Test
    void testRegisterUser_UsernameAlreadyExists() {
        Mockito.when(usersRepository.findByUsername(registerRequest.getUsername()))
                .thenReturn(Optional.of(user));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.registerUser(registerRequest);
        });
        assertEquals("Username already exists.", exception.getMessage());
    }

    @Test
    void testAuthUser_Success() {
        Mockito.when(usersRepository.findByUsername(loginRequest.getUsername()))
                .thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
                .thenReturn(true);
        Mockito.when(jwtTokenUtil.generateToken(user.getUsername()))
                .thenReturn("sample_jwt_token");
        String token = userService.authUser(loginRequest);
        assertNotNull(token);
        assertEquals("sample_jwt_token", token);
    }

    @Test
    void testAuthUser_InvalidCredentials() {
        Mockito.when(usersRepository.findByUsername(loginRequest.getUsername()))
                .thenReturn(Optional.of(user));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.authUser(loginRequest);
        });
        assertEquals("Invalid credentials", exception.getMessage());
    }
    }
