package com.barapp.web.business.impl;

import com.barapp.web.business.MobileNotification;
import com.barapp.web.business.service.NotificationService;
import com.barapp.web.business.service.UsuarioService;
import com.barapp.web.model.UsuarioApp;
import com.barapp.web.utils.FormatUtils;
import com.barapp.web.utils.scheduling.ScheduleManager;
import com.google.firebase.messaging.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FirebaseMessaging firebaseMessaging;
    private final UsuarioService usuarioService;
    private final ScheduleManager scheduleManager;

    public NotificationServiceImpl(FirebaseMessaging firebaseMessaging, UsuarioService usuarioService, ScheduleManager scheduleManager) {
        this.firebaseMessaging = firebaseMessaging;
        this.usuarioService = usuarioService;
        this.scheduleManager = scheduleManager;
    }

    @Override
    public void sendNotification(UsuarioApp usuario, MobileNotification notificacion) {
        Objects.requireNonNull(notificacion.getId(), "El id de la notificación no puede ser nulo");

        String idUsuario = usuario.getId();

        enviarNotificacionATokens(idUsuario, notificacion);
    }

    @Override
    public void scheduleNotificacion(UsuarioApp usuario, MobileNotification notificacion, LocalDateTime horario) {
        Objects.requireNonNull(notificacion.getId(), "El id de la notificación no puede ser nulo");

        String idUsuario = usuario.getId();

        if (horario.isBefore(LocalDateTime.now())) {
            logger.warn(
                    "No se puede programar una notificación en el pasado. Hora programada: {}",
                    horario.format(FormatUtils.timestampFormatter()));
            return;
        }

        scheduleManager.schedule(
                () -> enviarNotificacionATokens(idUsuario, notificacion),
                horario,
                notificacion.getId()
        );

        logger.info(
                "Notificacion programada para {}",
                horario.format(FormatUtils.timestampFormatter()));
    }

    @Override
    public void cancelNotificacion(String notificationId) {
        scheduleManager.cancel(notificationId);
    }

    @Override
    public boolean isNotificationScheduled(String notificationId) {
        return scheduleManager.isScheduled(notificationId);
    }

    private void enviarNotificacionATokens(String idUsuario, MobileNotification notificacion) {
        try {
            UsuarioApp usuario = usuarioService.get(idUsuario);

            for (String token : usuario.getFcmTokens()) {
                Status estado = enviarNotificacionAToken(token, notificacion);
                if (estado.equals(Status.UNREGISTERED_TOKEN)) {
                    usuario.getFcmTokens().remove(token);
                    usuarioService.save(usuario);
                }
            }
        } catch (Exception e) {
            logger.error("Error al actualizar el usuario {}", idUsuario, e);
        }
    }

    private Status enviarNotificacionAToken(String token, MobileNotification notificacion) {
        Message.Builder builder = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle(notificacion.getTitle())
                        .setBody(notificacion.getBody())
                        .setImage(notificacion.getImage())
                        .build())
                .setAndroidConfig(AndroidConfig.builder()
                        .setTtl(3600 * 1000)
                        .setNotification(AndroidNotification.builder()
                                .setColor("#e75a09") // Lumo primary color
                                .setTitleLocalizationKey(notificacion.getTitle_key())
                                .addAllTitleLocalizationArgs(notificacion.getTitle_args())
                                .setBodyLocalizationKey(notificacion.getBody_key())
                                .addAllBodyLocalizationArgs(notificacion.getBody_args())
                                .build())
                        .build())
                .setToken(token);
        if (!notificacion.getData().isEmpty()) {
            builder.putAllData(notificacion.getData());
        }

        Message message = builder.build();

        try {
            firebaseMessaging.send(message);
            logger.info("Mensaje enviado exitosamente. ID: {}", notificacion.getId());
            return Status.SUCCESS;
        } catch (FirebaseMessagingException e) {
            if (e.getMessagingErrorCode().equals(MessagingErrorCode.UNREGISTERED)) {
                logger.warn("Token no registrado detectado {}", token);
                return Status.UNREGISTERED_TOKEN;
            } else {
                logger.error("Error desconocido enviando mensaje", e);
                return Status.ERROR;
            }
        } catch (Exception e) {
            logger.error("Error desconocido enviando mensaje", e);
            return Status.ERROR;
        }
    }

    public enum Status {
        SUCCESS,
        UNREGISTERED_TOKEN,
        ERROR
    }
}
