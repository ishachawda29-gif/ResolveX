package com.resolvex.service;

import com.resolvex.dto.CreateTicketRequest;
import com.resolvex.model.Ticket;
import com.resolvex.model.TicketStatus;
import com.resolvex.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository repository;
    private final AiService aiService;

    public TicketService(TicketRepository repository, AiService aiService) {
        this.repository = repository;
        this.aiService = aiService;
    }

    public Ticket create(CreateTicketRequest request) {
        Ticket ticket = new Ticket();
        ticket.setCustomerName(request.customerName());
        ticket.setEmail(request.email());
        ticket.setSubject(request.subject());
        ticket.setDescription(request.description());

        aiService.analyze(ticket);
        return repository.save(ticket);
    }

    public List<Ticket> getAll(TicketStatus status) {
        return status == null ? repository.findAll() : repository.findByStatus(status);
    }

    public Ticket get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    public Ticket updateStatus(Long id, TicketStatus status) {
        Ticket ticket = get(id);
        ticket.setStatus(status);
        return repository.save(ticket);
    }

    public Ticket analyze(Long id) {
        Ticket ticket = get(id);
        aiService.analyze(ticket);
        return repository.save(ticket);
    }

    public long count() {
        return repository.count();
    }

    public long countByStatus(TicketStatus status) {
        return repository.findByStatus(status).size();
    }
}
