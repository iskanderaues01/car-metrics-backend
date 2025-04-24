package com.energo.car_metrics.services.impl;

import com.energo.car_metrics.models.Ticket;
import com.energo.car_metrics.models.UserAny;
import com.energo.car_metrics.models.enums.TicketStatus;
import com.energo.car_metrics.repositories.TicketRepository;
import com.energo.car_metrics.repositories.UserRepository;
import com.energo.car_metrics.utils.SecurityUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;



@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public TicketService(TicketRepository ticketRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    public List<Ticket> getTicketsForCurrentUser() {
        String username = SecurityUtils.getCurrentUsername();
        UserAny currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(role -> role.getName().name().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return ticketRepository.findAll();
        } else {
            return ticketRepository.findByCreatedBy(currentUser);
        }
    }

    // Создание новой заявки пользователем
    @Transactional
    public Ticket createTicket(Long userId, Ticket ticket) {
        UserAny user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        ticket.setCreatedBy(user);
        ticket.setStatus(TicketStatus.NEW);
        return ticketRepository.save(ticket);
    }

    // Редактирование заявки (разрешено только владельцу и только если заявка еще в статусе NEW)
    public Ticket updateTicket(Long ticketId, Ticket updatedTicketData) {
        String username = SecurityUtils.getCurrentUsername();
        UserAny currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(role -> role.getName().name().equals("ROLE_ADMIN"));

        if (!isAdmin && !ticket.getCreatedBy().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not allowed to update this ticket");
        }

        ticket.setTitle(updatedTicketData.getTitle());
        ticket.setDescription(updatedTicketData.getDescription());
        ticket.setStatus(updatedTicketData.getStatus());
        return ticketRepository.save(ticket);
    }

    public Ticket updateTicketStatus(Long ticketId, TicketStatus status) {
        String username = SecurityUtils.getCurrentUsername();
        UserAny currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(role -> role.getName().name().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new AccessDeniedException("Только администратор может менять статус заявки");
        }

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Заявка не найдена"));

        ticket.setStatus(status);
        return ticketRepository.save(ticket);
    }


    // Удаление заявки (только администратор)
    @Transactional
    public void deleteTicket(Long ticketId) {
        ticketRepository.deleteById(ticketId);
    }

    // Получение заявки по id
    public Ticket getTicket(Long ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Заявка не найдена"));
    }

    // Получение заявок конкретного пользователя
    public List<Ticket> getTicketsByUser(Long userId) {
        return ticketRepository.findByCreatedById(userId);
    }

    // Получение всех заявок (для администратора)
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }
}