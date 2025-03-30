package com.energo.car_metrics.services.impl;

import com.energo.car_metrics.models.Ticket;
import com.energo.car_metrics.models.UserAny;
import com.energo.car_metrics.models.enums.TicketStatus;
import com.energo.car_metrics.repositories.TicketRepository;
import com.energo.car_metrics.repositories.UserRepository;
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
    @Transactional
    public Ticket updateTicket(Long ticketId, Long userId, Ticket updatedTicket) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Заявка не найдена"));
        if (!ticket.getCreatedBy().getId().equals(userId)) {
            throw new RuntimeException("Нет прав на редактирование данной заявки");
        }
        if (ticket.getStatus() != TicketStatus.NEW) {
            throw new RuntimeException("Редактирование невозможно: заявка уже обработана");
        }
        ticket.setTitle(updatedTicket.getTitle());
        ticket.setDescription(updatedTicket.getDescription());
        return ticketRepository.save(ticket);
    }

    // Изменение статуса заявки модератором (например, на ACCEPTED или COMPLETED)
    @Transactional
    public Ticket updateTicketStatus(Long ticketId, TicketStatus status) {
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
