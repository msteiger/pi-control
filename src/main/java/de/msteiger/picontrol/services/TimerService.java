package de.msteiger.picontrol.services;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import it.sauronsoftware.cron4j.Scheduler;

/**
 * TODO: describe
 */
@Component
public class TimerService {

    private final Scheduler scheduler = new Scheduler();

    public TimerService() {
        scheduler.start();
    }

    /**
     * @param weekDays
     * @param time
     * @param runnable
     * @return
     */
    public String schedule(Set<DayOfWeek> weekDays, LocalTime time, Runnable runnable) {
      // 59 11 * * 1,2,3,4,5
      // This pattern causes a task to be launched at 11:59AM on Monday, Tuesday, Wednesday, Thursday and Friday.
      String wdText = weekDays.stream()
          .map(DayOfWeek::getValue)
          .map(String::valueOf)
          .collect(Collectors.joining(","));
      String pattern = String.format("%d %d * * %s", time.getMinute(), time.getHour(), wdText);
      String id = scheduler.schedule(pattern, runnable);
      return id;
    }

    /**
     * @param timerId
     */
    public void remove(String timerId) {
        scheduler.deschedule(timerId);
    }

    /**
     * @param list
     */
    public void removeAll(List<String> list) {
        if (list != null) {
            for (String timerId : list) {
                remove(timerId);
            }
        }
    }
}
