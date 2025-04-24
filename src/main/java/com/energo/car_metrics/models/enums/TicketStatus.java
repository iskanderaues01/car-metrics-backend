package com.energo.car_metrics.models.enums;


public enum TicketStatus {
    NEW,          // Новая заявка, только создана пользователем
    IN_PROGRESS,     // Заявка принята модератором
    DONE,    // Заявка выполнена модератором
}