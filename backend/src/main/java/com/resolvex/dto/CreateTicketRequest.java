package com.resolvex.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateTicketRequest(
        @NotBlank String customerName,
        @NotBlank @Email String email,
        @NotBlank String subject,
        @NotBlank String description
) {}
