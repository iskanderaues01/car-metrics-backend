package com.energo.car_metrics.models.enums;

public enum TicketStatus {
    NEW,          // Новая заявка, только создана пользователем
    ACCEPTED,     // Заявка принята модератором
    COMPLETED,    // Заявка выполнена модератором
    REJECTED      // Заявка отклонена (при необходимости)
}