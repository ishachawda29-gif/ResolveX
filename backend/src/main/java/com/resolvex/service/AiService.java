package com.resolvex.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.resolvex.model.Ticket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class AiService {

    private final RestClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    public AiService(@Value("${ai.service.url}") String baseUrl) {
        this.client = RestClient.builder().baseUrl(baseUrl).build();
    }

    public void analyze(Ticket ticket) {
        try {
            String body = client.post()
                    .uri("/analyze")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of(
                            "customerName", ticket.getCustomerName(),
                            "subject", ticket.getSubject(),
                            "description", ticket.getDescription()
                    ))
                    .retrieve()
                    .body(String.class);

            JsonNode json = mapper.readTree(body);
            ticket.setCategory(json.path("category").asText("GENERAL"));
            ticket.setPriority(com.resolvex.model.TicketPriority.valueOf(
                    json.path("priority").asText("MEDIUM")
            ));
            ticket.setAiReply(json.path("suggestedReply").asText(""));
        } catch (Exception e) {
            ticket.setCategory("GENERAL");
            ticket.setPriority(com.resolvex.model.TicketPriority.MEDIUM);
            ticket.setAiReply("AI service is currently unavailable. A support agent should review this ticket manually.");
        }
    }
}
