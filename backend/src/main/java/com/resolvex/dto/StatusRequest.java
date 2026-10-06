package com.resolvex.dto;

import com.resolvex.model.TicketStatus;
import jakarta.validation.constraints.NotNull;

public record StatusRequest(@NotNull TicketStatus status) {}
