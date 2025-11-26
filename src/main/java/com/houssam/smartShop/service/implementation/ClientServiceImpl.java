package com.houssam.smartShop.service.implementation;

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
import com.houssam.smartShop.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ClientMapper clientMapper;

    @Transactional
    public ClientResponseDTO createClient(ClientRequestDTO dto){
        String hashedPassword = BCrypt.hashpw(dto.getUser().getPassword(), BCrypt.gensalt());

        User user = User.builder()
                .username(dto.getUser().getUsername())
                .password(hashedPassword)
                .role(UserRole.CLIENT)
                .build();
        user = userRepository.save(user);

        Client client = Client.builder()
                .nom(dto.getNom())
                .prenom(dto.getPrenom())
                .email(dto.getEmail())
                .customerTier(CustomerTier.BASIC)
                .totalOrders(0)
                .totalSpent(0.0)
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
