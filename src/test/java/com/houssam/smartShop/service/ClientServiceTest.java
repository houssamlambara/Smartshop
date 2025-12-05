package com.houssam.smartShop.service;

import com.houssam.smartShop.dto.requestDTO.ClientRequestDTO;
import com.houssam.smartShop.dto.requestDTO.UserRequestDTO;
import com.houssam.smartShop.dto.responseDTO.ClientResponseDTO;
import com.houssam.smartShop.enums.CustomerTier;
import com.houssam.smartShop.mapper.ClientMapper;
import com.houssam.smartShop.model.Client;
import com.houssam.smartShop.model.User;
import com.houssam.smartShop.repository.ClientRepository;
import com.houssam.smartShop.repository.UserRepository;
import com.houssam.smartShop.service.implementation.ClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {
    @Mock
    private ClientRepository clientRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private ClientServiceImpl clientService;

    private Client client;
    private ClientRequestDTO clientRequestDTO;
    private ClientResponseDTO clientResponseDTO;
    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id("user-1")
                .username("testuser")
                .password("password")
                .build();

        client = Client.builder()
                .id("client-1")
                .nom("Dupont")
                .prenom("Jean")
                .email("jean.dupont@email.com")
                .customerTier(CustomerTier.BASIC)
                .totalOrders(0)
                .totalSpent(0.0)
                .user(user)
                .build();

        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setUsername("testuser");
        userRequestDTO.setPassword("password");

        clientRequestDTO = new ClientRequestDTO();
        clientRequestDTO.setNom("Dupont");
        clientRequestDTO.setPrenom("Jean");
        clientRequestDTO.setEmail("jean.dupont@email.com");
        clientRequestDTO.setUser(userRequestDTO);

        clientResponseDTO = new ClientResponseDTO();
        clientResponseDTO.setId("client-1");
        clientResponseDTO.setNom("Dupont");
        clientResponseDTO.setPrenom("Jean");
        clientResponseDTO.setEmail("jean.dupont@email.com");
    }

    @Test
    void createClient_ShouldReturnClientResponseDTO() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(clientRepository.save(any(Client.class))).thenReturn(client);
        when(clientMapper.toResponse(any(Client.class))).thenReturn(clientResponseDTO);

        ClientResponseDTO result = clientService.createClient(clientRequestDTO);

        assertNotNull(result);
        assertEquals("Dupont", result.getNom());
        assertEquals("Jean", result.getPrenom());
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void getAllClients_ShouldReturnPageOfClients() {
        int page = 0;
        int size = 10;

        Page<Client> clientPage = new PageImpl<>(Arrays.asList(client));
        when(clientRepository.findAll(any(PageRequest.class))).thenReturn(clientPage);
        when(clientMapper.toResponse(any(Client.class))).thenReturn(clientResponseDTO);

        Page<ClientResponseDTO> result = clientService.getAllClients(page, size);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Dupont", result.getContent().get(0).getNom());
    }

    @Test
    void getClientById_ShouldReturnClient() {
        when(clientRepository.findById("client-1")).thenReturn(Optional.of(client));
        when(clientMapper.toResponse(client)).thenReturn(clientResponseDTO);

        ClientResponseDTO result = clientService.getClientById("client-1");

        assertNotNull(result);
        assertEquals("client-1", result.getId());
        assertEquals("Dupont", result.getNom());
        verify(clientRepository, times(1)).findById("client-1");
    }

    @Test
    void deleteClient_ShouldDeleteClient() {
        when(clientRepository.findById("client-1")).thenReturn(Optional.of(client));
        doNothing().when(clientRepository).delete(client);

        clientService.deleteClient("client-1");

        verify(clientRepository, times(1)).delete(client);
    }
}

