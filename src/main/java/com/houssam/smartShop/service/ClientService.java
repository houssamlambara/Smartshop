package com.houssam.smartShop.service;

import com.houssam.smartShop.dto.requestDTO.ClientRequestDTO;
import com.houssam.smartShop.dto.responseDTO.ClientResponseDTO;
import com.houssam.smartShop.dto.responseDTO.OrderResponseDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ClientService {

    ClientResponseDTO createClient(ClientRequestDTO dto);

    ClientResponseDTO getClientById(String id);

    Page<ClientResponseDTO> getAllClients(int page, int size);

    ClientResponseDTO updateClient(String id, ClientRequestDTO dto);

    void deleteClient(String id);

    List<OrderResponseDTO> getClientOrders(String id);

    ClientResponseDTO getMyProfile(HttpSession session);

    ClientResponseDTO updateMyProfile(ClientRequestDTO dto, HttpSession session);

    List<OrderResponseDTO> getMyOrders(HttpSession session);
}
