package com.resolvex.controller;

import com.resolvex.dto.CreateTicketRequest;
import com.resolvex.dto.StatusRequest;
import com.resolvex.model.Ticket;
import com.resolvex.model.TicketStatus;
import com.resolvex.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class TicketController {

    private final TicketService service;

    public TicketController(TicketService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Ticket create(@Valid @RequestBody CreateTicketRequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<Ticket> getAll(@RequestParam(required = false) TicketStatus status) {
        return service.getAll(status);
    }

    @GetMapping("/{id}")
    public Ticket get(@PathVariable Long id) {
        return service.get(id);
    }

    @PatchMapping("/{id}/status")
    public Ticket updateStatus(@PathVariable Long id,
                                @Valid @RequestBody StatusRequest request) {
        return service.updateStatus(id, request.status());
    }

    @PostMapping("/{id}/analyze")
    public Ticket analyze(@PathVariable Long id) {
        return service.analyze(id);
    }

    @GetMapping("/stats")
    public Map<String, Long> stats() {
        return Map.of(
                "total", service.count(),
                "open", service.countByStatus(TicketStatus.OPEN),
                "inProgress", service.countByStatus(TicketStatus.IN_PROGRESS),
                "resolved", service.countByStatus(TicketStatus.RESOLVED),
                "closed", service.countByStatus(TicketStatus.CLOSED)
        );
    }
}
