package com.houssam.smartShop.service;

import com.houssam.smartShop.dto.requestDTO.LoginRequestDTO;
import com.houssam.smartShop.dto.responseDTO.LoginResponseDTO;
import com.houssam.smartShop.enums.UserRole;
import com.houssam.smartShop.exception.ResourceNotFoundException;
import com.houssam.smartShop.model.User;
import com.houssam.smartShop.repository.UserRepository;
import com.houssam.smartShop.service.implementation.AuthServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpSession httpSession;

    @InjectMocks
    private AuthServiceImpl authService;

    private User user;
    private LoginRequestDTO loginRequestDTO;

    @BeforeEach
    void setUp() {

        String hashedPassword = BCrypt.hashpw("password123", BCrypt.gensalt());

        user = User.builder()
                .id("user-1")
                .username("testuser")
                .password(hashedPassword)
                .role(UserRole.ADMIN)
                .build();

        loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setUsername("testuser");
        loginRequestDTO.setPassword("password123");
    }

    @Test
    void login_WithValidCredentials_ShouldReturnLoginResponse() {

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(httpServletRequest.getSession(true)).thenReturn(httpSession);
        doNothing().when(httpSession).setAttribute(anyString(), any());

        LoginResponseDTO result = authService.login(loginRequestDTO, httpServletRequest);

        assertNotNull(result);
        assertEquals("user-1", result.getUserId());
        assertEquals("testuser", result.getUsername());
        assertEquals(UserRole.ADMIN, result.getRole());
        assertEquals("Connexion réussie", result.getMessage());

        verify(httpSession, times(1)).setAttribute("userId", "user-1");
        verify(httpSession, times(1)).setAttribute("username", "testuser");
        verify(httpSession, times(1)).setAttribute("role", UserRole.ADMIN);
    }

    @Test
    void login_WithInvalidUsername_ShouldThrowException() {

        when(userRepository.findByUsername("invaliduser")).thenReturn(Optional.empty());

        LoginRequestDTO invalidRequest = new LoginRequestDTO();
        invalidRequest.setUsername("invaliduser");
        invalidRequest.setPassword("password123");

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            authService.login(invalidRequest, httpServletRequest);
        });

        assertEquals("Username ou mot de passe incorrect", exception.getMessage());
        verify(httpServletRequest, never()).getSession(true);
    }

    @Test
    void login_WithInvalidPassword_ShouldThrowException() {

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        LoginRequestDTO invalidRequest = new LoginRequestDTO();
        invalidRequest.setUsername("testuser");
        invalidRequest.setPassword("wrongpassword");

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            authService.login(invalidRequest, httpServletRequest);
        });

        assertEquals("Username ou mot de passe incorrect", exception.getMessage());
        verify(httpServletRequest, never()).getSession(true);
    }

    @Test
    void logout_WithValidSession_ShouldInvalidateSession() {

        when(httpServletRequest.getSession(false)).thenReturn(httpSession);
        doNothing().when(httpSession).invalidate();
        authService.logout(httpServletRequest);

        verify(httpSession, times(1)).invalidate();
    }

    @Test
    void logout_WithNoSession_ShouldDoNothing() {

        when(httpServletRequest.getSession(false)).thenReturn(null);
        authService.logout(httpServletRequest);

        verify(httpSession, never()).invalidate();
    }

    @Test
    void getCurrentUser_WithValidSession_ShouldReturnUser() {

        when(httpServletRequest.getSession(false)).thenReturn(httpSession);
        when(httpSession.getAttribute("userId")).thenReturn("user-1");
        when(userRepository.findById("user-1")).thenReturn(Optional.of(user));
        LoginResponseDTO result = authService.getCurrentUser(httpServletRequest);

        assertNotNull(result);
        assertEquals("user-1", result.getUserId());
        assertEquals("testuser", result.getUsername());
        assertEquals(UserRole.ADMIN, result.getRole());
        assertEquals("Utilisateur connecté", result.getMessage());
    }

    @Test
    void getCurrentUser_WithNoSession_ShouldThrowException() {

        when(httpServletRequest.getSession(false)).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            authService.getCurrentUser(httpServletRequest);
        });

        assertEquals("Aucun utilisateur connecté", exception.getMessage());
    }

    @Test
    void getCurrentUser_WithInvalidUserId_ShouldThrowException() {

        when(httpServletRequest.getSession(false)).thenReturn(httpSession);
        when(httpSession.getAttribute("userId")).thenReturn("invalid-id");
        when(userRepository.findById("invalid-id")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            authService.getCurrentUser(httpServletRequest);
        });

        assertEquals("Utilisateur non trouvé", exception.getMessage());
    }

    @Test
    void login_WithClientRole_ShouldSucceed() {

        String hashedPassword = BCrypt.hashpw("clientpass", BCrypt.gensalt());
        User clientUser = User.builder()
                .id("client-user-1")
                .username("clientuser")
                .password(hashedPassword)
                .role(UserRole.CLIENT)
                .build();

        LoginRequestDTO clientRequest = new LoginRequestDTO();
        clientRequest.setUsername("clientuser");
        clientRequest.setPassword("clientpass");

        when(userRepository.findByUsername("clientuser")).thenReturn(Optional.of(clientUser));
        when(httpServletRequest.getSession(true)).thenReturn(httpSession);
        doNothing().when(httpSession).setAttribute(anyString(), any());
        LoginResponseDTO result = authService.login(clientRequest, httpServletRequest);

        assertNotNull(result);
        assertEquals(UserRole.CLIENT, result.getRole());
        assertEquals("Connexion réussie", result.getMessage());
        verify(httpSession, times(1)).setAttribute("role", UserRole.CLIENT);
    }
}

