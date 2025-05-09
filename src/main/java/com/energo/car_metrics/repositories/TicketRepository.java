package com.energo.car_metrics.repositories;

import com.energo.car_metrics.models.Ticket;
import com.energo.car_metrics.models.UserAny;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByCreatedById(Long userId);

    List<Ticket> findByCreatedBy(UserAny user);
}