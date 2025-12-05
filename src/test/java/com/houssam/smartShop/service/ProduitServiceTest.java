package com.houssam.smartShop.service;

import com.houssam.smartShop.dto.requestDTO.ProduitRequestDTO;
import com.houssam.smartShop.dto.responseDTO.ProduitResponseDTO;
import com.houssam.smartShop.exception.ResourceNotFoundException;
import com.houssam.smartShop.mapper.ProduitMapper;
import com.houssam.smartShop.model.Produit;
import com.houssam.smartShop.repository.ProduitRepository;
import com.houssam.smartShop.service.implementation.ProduitServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProduitServiceTest {

    @Mock
    private ProduitRepository produitRepository;

    @Mock
    private ProduitMapper produitMapper;

    @InjectMocks
    private ProduitServiceImpl produitService;

    private Produit produit;
    private ProduitRequestDTO produitRequestDTO;
    private ProduitResponseDTO produitResponseDTO;

    @BeforeEach
    void setUp() {

        produit = Produit.builder()
                .id("produit-1")
                .nom("Laptop HP")
                .prixUnite(5000.0)
                .stock(10)
                .delete(false)
                .build();

        produitRequestDTO = new ProduitRequestDTO();
        produitRequestDTO.setNom("Laptop HP");
        produitRequestDTO.setPrixUnite(5000.0);
        produitRequestDTO.setStock(10);

        produitResponseDTO = new ProduitResponseDTO();
        produitResponseDTO.setId("produit-1");
        produitResponseDTO.setNom("Laptop HP");
        produitResponseDTO.setPrixUnite(5000.0);
        produitResponseDTO.setStock(10);
    }

    @Test
    void createProduit_ShouldReturnProduitResponseDTO() {

        when(produitMapper.toEntity(any(ProduitRequestDTO.class))).thenReturn(produit);
        when(produitRepository.save(any(Produit.class))).thenReturn(produit);
        when(produitMapper.toResponse(any(Produit.class))).thenReturn(produitResponseDTO);
        ProduitResponseDTO result = produitService.createProduit(produitRequestDTO);

        assertNotNull(result);
        assertEquals("Laptop HP", result.getNom());
        assertEquals(5000.0, result.getPrixUnite());
        assertEquals(10, result.getStock());
        verify(produitRepository, times(1)).save(any(Produit.class));
    }

    @Test
    void getProduitById_ShouldReturnProduit() {

        when(produitRepository.findById("produit-1")).thenReturn(Optional.of(produit));
        when(produitMapper.toResponse(produit)).thenReturn(produitResponseDTO);
        ProduitResponseDTO result = produitService.getProduitById("produit-1");

        assertNotNull(result);
        assertEquals("produit-1", result.getId());
        assertEquals("Laptop HP", result.getNom());
        verify(produitRepository, times(1)).findById("produit-1");
    }

    @Test
    void getProduitById_ShouldThrowException_WhenNotFound() {

        when(produitRepository.findById("invalid-id")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            produitService.getProduitById("invalid-id");
        });
        verify(produitRepository, times(1)).findById("invalid-id");
    }

    @Test
    void getAllProduits_ShouldReturnListOfProduits() {

        List<Produit> produits = Arrays.asList(produit);
        when(produitRepository.findAll()).thenReturn(produits);
        when(produitMapper.toResponse(any(Produit.class))).thenReturn(produitResponseDTO);
        List<ProduitResponseDTO> result = produitService.getAllProduits();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Laptop HP", result.get(0).getNom());
        verify(produitRepository, times(1)).findAll();
    }

    @Test
    void updateProduit_ShouldReturnUpdatedProduit() {

        ProduitRequestDTO updateDTO = new ProduitRequestDTO();
        updateDTO.setNom("Laptop Dell");
        updateDTO.setPrixUnite(6000.0);
        updateDTO.setStock(15);

        Produit updatedProduit = Produit.builder()
                .id("produit-1")
                .nom("Laptop Dell")
                .prixUnite(6000.0)
                .stock(15)
                .delete(false)
                .build();

        ProduitResponseDTO updatedResponseDTO = new ProduitResponseDTO();
        updatedResponseDTO.setId("produit-1");
        updatedResponseDTO.setNom("Laptop Dell");
        updatedResponseDTO.setPrixUnite(6000.0);
        updatedResponseDTO.setStock(15);

        when(produitRepository.findById("produit-1")).thenReturn(Optional.of(produit));
        doNothing().when(produitMapper).updateEntity(any(ProduitRequestDTO.class), any(Produit.class));
        when(produitRepository.save(any(Produit.class))).thenReturn(updatedProduit);
        when(produitMapper.toResponse(any(Produit.class))).thenReturn(updatedResponseDTO);
        ProduitResponseDTO result = produitService.updateProduit("produit-1", updateDTO);

        assertNotNull(result);
        assertEquals("Laptop Dell", result.getNom());
        assertEquals(6000.0, result.getPrixUnite());
        assertEquals(15, result.getStock());
        verify(produitRepository, times(1)).findById("produit-1");
        verify(produitRepository, times(1)).save(any(Produit.class));
    }

    @Test
    void deleteProduit_ShouldMarkAsDeleted() {

        when(produitRepository.findById("produit-1")).thenReturn(Optional.of(produit));
        when(produitRepository.save(any(Produit.class))).thenReturn(produit);
        produitService.deleteProduit("produit-1");

        verify(produitRepository, times(1)).findById("produit-1");
        verify(produitRepository, times(1)).save(any(Produit.class));
        assertTrue(produit.getDelete());
    }

    @Test
    void deleteProduit_ShouldThrowException_WhenNotFound() {

        when(produitRepository.findById("invalid-id")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            produitService.deleteProduit("invalid-id");
        });
        verify(produitRepository, times(1)).findById("invalid-id");
        verify(produitRepository, never()).save(any(Produit.class));
    }

    @Test
    void createProduit_WithZeroStock_ShouldSucceed() {

        ProduitRequestDTO zeroStockDTO = new ProduitRequestDTO();
        zeroStockDTO.setNom("Produit Test");
        zeroStockDTO.setPrixUnite(100.0);
        zeroStockDTO.setStock(0);

        Produit zeroStockProduit = Produit.builder()
                .id("produit-2")
                .nom("Produit Test")
                .prixUnite(100.0)
                .stock(0)
                .delete(false)
                .build();

        ProduitResponseDTO zeroStockResponse = new ProduitResponseDTO();
        zeroStockResponse.setId("produit-2");
        zeroStockResponse.setNom("Produit Test");
        zeroStockResponse.setPrixUnite(100.0);
        zeroStockResponse.setStock(0);

        when(produitMapper.toEntity(any(ProduitRequestDTO.class))).thenReturn(zeroStockProduit);
        when(produitRepository.save(any(Produit.class))).thenReturn(zeroStockProduit);
        when(produitMapper.toResponse(any(Produit.class))).thenReturn(zeroStockResponse);
        ProduitResponseDTO result = produitService.createProduit(zeroStockDTO);

        assertNotNull(result);
        assertEquals(0, result.getStock());
        verify(produitRepository, times(1)).save(any(Produit.class));
    }
}

