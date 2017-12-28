package de.msteiger.picontrol.model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.EnumSet;
import java.util.Set;

/**
 * TODO: describe
 */
public class TriggerTime {

    private Set<DayOfWeek> weekDays = EnumSet.noneOf(DayOfWeek.class);
    private LocalTime time;

    /**
     * @return the weekDays
     */
    public Set<DayOfWeek> getWeekDays() {
        return weekDays;
    }
    /**
     * @param weekDays the weekDays to set
     */
    public void setWeekDays(Set<DayOfWeek> weekDays) {
        this.weekDays = weekDays;
    }
    /**
     * @return the time
     */
    public LocalTime getTime() {
        return time;
    }
    /**
     * @param time the time to set
     */
    public void setTime(LocalTime time) {
        this.time = time;
    }
}
