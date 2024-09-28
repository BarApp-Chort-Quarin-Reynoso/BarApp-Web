package com.barapp.web.business.service;

import com.barapp.web.business.MobileNotification;
import com.barapp.web.model.UsuarioApp;

import java.time.LocalDateTime;

public interface NotificationService {
    void sendNotification(UsuarioApp usuario, MobileNotification notificacion);
    void scheduleNotificacion(UsuarioApp usuario, MobileNotification notificacion, LocalDateTime horario);
    void cancelNotificacion(String notificationId);
    boolean isNotificationScheduled(String notificationId);
}
