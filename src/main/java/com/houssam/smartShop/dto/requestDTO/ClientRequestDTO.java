package com.houssam.smartShop.dto.requestDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientRequestDTO {
    @NotBlank(message = "Le nom du client est obligatoire")
    @Size(min = 2, message = "Le nom du client doit contenir au moins 2 caractères")
    private String nom;

    @NotBlank(message = "Le prénom du client est obligatoire")
    @Size(min = 2, message = "Le prénom du client doit contenir au moins 2 caractères")
    private String prenom;

    @NotBlank(message = "L'email du client est obligatoire")
    @Size(min = 5, message = "L'email du client doit contenir au moins 5 caractères")
    private String email;

    @Valid
    @NotNull(message = "L'adresse du client est obligatoire")
    private UserRequestDTO user;
}
