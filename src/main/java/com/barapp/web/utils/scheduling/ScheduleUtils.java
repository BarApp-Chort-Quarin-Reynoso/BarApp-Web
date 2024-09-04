package com.barapp.web.utils.scheduling;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ScheduleUtils {
    public static ScheduledThreadPoolExecutor createScheduledExecutor() {
        final ScheduledThreadPoolExecutor executor
                = new ScheduledThreadPoolExecutor(1);

        Runnable task = executor::purge;

        executor.scheduleWithFixedDelay(task, 15, 15, TimeUnit.MINUTES);

        return executor;
    }
}
