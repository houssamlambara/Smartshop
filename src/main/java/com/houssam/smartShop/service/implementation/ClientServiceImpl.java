package com.houssam.smartShop.service.implementation;

import com.houssam.smartShop.dto.requestDTO.ClientRequestDTO;
import com.houssam.smartShop.dto.responseDTO.ClientResponseDTO;
import com.houssam.smartShop.dto.responseDTO.OrderResponseDTO;
import com.houssam.smartShop.enums.CustomerTier;
import com.houssam.smartShop.enums.UserRole;
import com.houssam.smartShop.exception.ResourceNotFoundException;
import com.houssam.smartShop.mapper.ClientMapper;
import com.houssam.smartShop.mapper.OrderMapper;
import com.houssam.smartShop.model.Client;
import com.houssam.smartShop.model.Order;
import com.houssam.smartShop.model.User;
import com.houssam.smartShop.repository.ClientRepository;
import com.houssam.smartShop.repository.OrderRepository;
import com.houssam.smartShop.repository.UserRepository;
import com.houssam.smartShop.service.ClientService;
import jakarta.servlet.http.HttpSession;
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
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

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

    public List<OrderResponseDTO> getClientOrders(String clientId) {
        clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé avec ID : " + clientId));

        List<Order> orders = orderRepository.findByClientId(clientId);

        return orders.stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    private Client getClientFromSession(HttpSession session){
        String userId = (String) session.getAttribute("userId");
        if(userId == null){
            throw new RuntimeException("Utilisateur non connecté");
        }
        return clientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé"));
    }

    @Override
    public ClientResponseDTO getMyProfile(HttpSession session){
        Client client = getClientFromSession(session);
        return clientMapper.toResponse(client);
    }

    @Transactional
    @Override
    public ClientResponseDTO updateMyProfile(ClientRequestDTO dto, HttpSession session){
        Client client = getClientFromSession(session);
        clientMapper.updateEntityFromDTO(dto, client);
        client = clientRepository.save(client);
        return clientMapper.toResponse(client);
    }

    @Override
    public List<OrderResponseDTO> getMyOrders(HttpSession session){
        Client client = getClientFromSession(session);
        List<Order> orders = orderRepository.findByClientId(client.getId());
        return orders.stream().map(orderMapper::toResponse).collect(Collectors.toList());
    }

}
