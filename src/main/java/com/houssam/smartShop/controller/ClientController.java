package com.houssam.smartShop.controller;

import com.houssam.smartShop.dto.requestDTO.ClientRequestDTO;
import com.houssam.smartShop.dto.responseDTO.ClientResponseDTO;
import com.houssam.smartShop.model.Client;
import com.houssam.smartShop.response.ApiResponse;
import com.houssam.smartShop.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ApiResponse<ClientResponseDTO>> createClient(@Valid @RequestBody ClientRequestDTO dto){
        ClientResponseDTO client = clientService.createClient(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Client cree avec succes", client));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClientResponseDTO>>> getAllClients(){
        List<ClientResponseDTO> clients = clientService.getAllClients();
        return ResponseEntity.ok(new ApiResponse<>("Liste des clients", clients));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientResponseDTO>>getClientById(@PathVariable String id){
        ClientResponseDTO client = clientService.getClientById(id);
        return ResponseEntity.ok(new ApiResponse<>("Details du client", client));   }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientResponseDTO>> updateClient(@PathVariable String id, @Valid @RequestBody ClientRequestDTO dto){
        ClientResponseDTO client = clientService.updateClient(id, dto);
        return ResponseEntity.ok(new ApiResponse<>("Client mis a jour avec succes", client));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteClient(@PathVariable String id){
        clientService.deleteClient(id);
        return ResponseEntity.ok (new ApiResponse<>("Client supprime avec succes", null));
    }

}
