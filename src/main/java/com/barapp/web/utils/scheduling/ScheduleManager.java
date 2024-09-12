package com.barapp.web.utils.scheduling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

@Component
@Scope(scopeName = "singleton")
public class ScheduleManager {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ScheduledExecutorService scheduler;

    private final Map<String, ScheduledFuture<?>> futures;

    public ScheduleManager() {
        this.scheduler = ScheduleUtils.createScheduledExecutor();
        futures = new HashMap<>();
    }

    /**
     * Programa una tarea para ser ejecutada en el horario especificado.
     * @param tarea la tarea a ejecutar
     * @param horario la fecha y hora en la que se ejecutará la tarea
     * @param id el id de la tarea
     */
    public void schedule(Runnable tarea, LocalDateTime horario, String id) {
        if (futures.containsKey(id)) {
            logger.warn("Ya existe una tarea con el id {}", id);
            return;
        }

        ScheduledFuture<?> future = scheduler.schedule(
                tarea,
                Duration.between(LocalDateTime.now(), horario).getSeconds(),
                java.util.concurrent.TimeUnit.SECONDS
        );
        futures.put(id, future);
        logger.debug("Tarea programada para el id {} con fecha y hora {}", id, horario.toString());
    }

    /**
     * Cancela la tarea programada con el id especificado. Si no existe una tarea con ese id, no hace nada.
     * @param id el id de la tarea
     */
    public void cancel(String id) {
        ScheduledFuture<?> future = futures.get(id);
        if (future != null) {
            future.cancel(true);
            futures.remove(id);

            logger.debug("Tarea cancelada para el id {}", id);
        }
    }

    /**
     * Verifica si una tarea con el id especificado está programada.
     * @param id el id de la tarea
     * @return true si la tarea está programada, false en caso contrario
     */
    public boolean isScheduled(String id) {
        return futures.containsKey(id);
    }
}
