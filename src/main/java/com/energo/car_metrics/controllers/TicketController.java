package com.energo.car_metrics.controllers;

import com.energo.car_metrics.models.Ticket;
import com.energo.car_metrics.models.enums.TicketStatus;
import com.energo.car_metrics.services.impl.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    // Создание новой заявки (пользователь)
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'MOD', 'ADMIN')")
    public ResponseEntity<Ticket> createTicket(@RequestParam Long userId, @RequestBody Ticket ticket) {
        Ticket createdTicket = ticketService.createTicket(userId, ticket);
        return ResponseEntity.ok(createdTicket);
    }

    @PutMapping("/{ticketId}")
    @PreAuthorize("hasAnyRole('USER', 'MOD', 'ADMIN')")
    public ResponseEntity<Ticket> updateTicket(@PathVariable Long ticketId,
                                               @RequestBody Ticket ticket) {
        Ticket updatedTicket = ticketService.updateTicket(ticketId, ticket);
        return ResponseEntity.ok(updatedTicket);
    }

    @PutMapping("/{ticketId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Ticket> updateStatus(@PathVariable Long ticketId,
                                               @RequestParam TicketStatus status) {
        Ticket updatedTicket = ticketService.updateTicketStatus(ticketId, status);
        return ResponseEntity.ok(updatedTicket);
    }

    // Удаление заявки (только администратор)
    @DeleteMapping("/{ticketId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long ticketId) {
        ticketService.deleteTicket(ticketId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'MOD', 'ADMIN')")
    public ResponseEntity<List<Ticket>> getTickets() {
        List<Ticket> tickets = ticketService.getTicketsForCurrentUser();
        return ResponseEntity.ok(tickets);
    }
}