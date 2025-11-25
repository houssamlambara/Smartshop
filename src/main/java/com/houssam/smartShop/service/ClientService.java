package com.houssam.smartShop.service;

import com.houssam.smartShop.dto.requestDTO.ClientRequestDTO;
import com.houssam.smartShop.dto.responseDTO.ClientResponseDTO;
import com.houssam.smartShop.enums.CustomerTier;
import com.houssam.smartShop.enums.UserRole;
import com.houssam.smartShop.exception.ResourceNotFoundException;
import com.houssam.smartShop.mapper.ClientMapper;
import com.houssam.smartShop.model.Client;
import com.houssam.smartShop.model.User;
import com.houssam.smartShop.repository.ClientRepository;
import com.houssam.smartShop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ClientMapper clientMapper;

    @Transactional
    public ClientResponseDTO createClient(ClientRequestDTO dto){
        User user = User.builder()
                .username(dto.getUser().getUsername())
                .password(dto.getUser().getPassword())
                .role(UserRole.CLIENT)
                .build();
        user = userRepository.save(user);

        Client client = Client.builder()
                .nom(dto.getNom())
                .prenom(dto.getPrenom())
                .email(dto.getEmail())
                .customerTier(CustomerTier.BASIC)
                .totalOrders(0)
                .totalSpent(BigDecimal.ZERO)
                .user(user)
                .build();

        client = clientRepository.save(client);

        return clientMapper.toResponse(client);
    }

    public ClientResponseDTO getClientById(String id){
        Client client = clientRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Client non trouvé avec ID:"+id));
    return clientMapper.toResponse(client);
    }

    public List<ClientResponseDTO> getAllClients(){
        return clientRepository.findAll()
                .stream()
                .map(clientMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ClientResponseDTO updateClient(String id, ClientRequestDTO dto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé"));

        // Mettre à jour
        clientMapper.updateEntityFromDTO(dto, client);
        client = clientRepository.save(client);

        return clientMapper.toResponse(client);
    }


    @Transactional
    public void deleteClient(String id){
        Client client = clientRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Client non trouvé avec ID:"+id));
        clientRepository.delete(client);
    }

}
