package com.houssam.smartShop.controller;

import com.houssam.smartShop.dto.requestDTO.ClientRequestDTO;
import com.houssam.smartShop.dto.responseDTO.ClientResponseDTO;
import com.houssam.smartShop.dto.responseDTO.OrderResponseDTO;
import com.houssam.smartShop.response.ApiResponse;
import com.houssam.smartShop.service.implementation.ClientServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/admin/clients")  // Préfixe commun pour ADMIN
@RestController
@RequiredArgsConstructor
public class ClientController {

    private final ClientServiceImpl clientServiceImpl;

    @PostMapping
    public ResponseEntity<ApiResponse<ClientResponseDTO>> createClient(@Valid @RequestBody ClientRequestDTO dto){
        ClientResponseDTO client = clientServiceImpl.createClient(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Client cree avec succes", client));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClientResponseDTO>>> getAllClients(){
        List<ClientResponseDTO> clients = clientServiceImpl.getAllClients();
        return ResponseEntity.ok(new ApiResponse<>("Liste des clients", clients));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientResponseDTO>>getClientById(@PathVariable String id){
        ClientResponseDTO client = clientServiceImpl.getClientById(id);
        return ResponseEntity.ok(new ApiResponse<>("Details du client", client));   }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientResponseDTO>> updateClient(@PathVariable String id, @Valid @RequestBody ClientRequestDTO dto){
        ClientResponseDTO client = clientServiceImpl.updateClient(id, dto);
        return ResponseEntity.ok(new ApiResponse<>("Client mis a jour avec succes", client));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteClient(@PathVariable String id){
        clientServiceImpl.deleteClient(id);
        return ResponseEntity.ok (new ApiResponse<>("Client supprime avec succes", null));
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getClientOrders(@PathVariable String id){
        List<OrderResponseDTO> orders = clientServiceImpl.getClientOrders(id);
        return ResponseEntity.ok(new ApiResponse<>("Liste des commandes du client", orders));
    }

}
