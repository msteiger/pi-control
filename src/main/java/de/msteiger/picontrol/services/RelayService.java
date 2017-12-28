package de.msteiger.picontrol.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.msteiger.picontrol.model.RelayInfo;
import de.msteiger.picontrol.model.TriggerTime;

/**
 * TODO: describe
 */
@Component
public class RelayService {

    private static final Logger logger = LoggerFactory.getLogger(RelayService.class);

    @Autowired
    private PersistenceService persistenceService;

    @Autowired
    private TimerService timerService;

    @Autowired
    private GpioService gpioService;

    private Map<String, RelayInfo> relayInfos;
    private Map<String, List<String>> timerIds = new HashMap<>();

    @PostConstruct
    private void init() throws IOException {
        relayInfos = persistenceService.loadRelays();
        for (Entry<String, RelayInfo> entry : relayInfos.entrySet()) {
            String id = entry.getKey();
            RelayInfo value = entry.getValue();
            updateSchedules(id, value);
        }
    }

    /**
     * @param id
     * @param value
     */
    private void updateSchedules(String id, RelayInfo value) {
        List<String> timerIdList = new ArrayList<>();
        List<String> oldTimers = timerIds.put(id, timerIdList);

        timerService.removeAll(oldTimers);

        for (TriggerTime tt : value.getTriggers()) {
            String timerId = timerService.schedule(tt.getWeekDays(), tt.getTime(), () -> toggleRelay(id));
            timerIdList.add(timerId);
            logger.debug("Scheduled timer '{}' at '{}'", id, tt);
        }
    }

    /**
     * @param id
     * @param ri
     */
    public void saveRelay(String id, RelayInfo ri) throws IOException {
        relayInfos.put(id, ri);
        persistenceService.storeRelays(relayInfos);
        updateSchedules(id, ri);
    }

    /**
     * @param id
     * @return
     */
    public RelayInfo getRelay(String id) {
        return relayInfos.get(id);
    }

    /**
     * @param id
     * @return
     */
    public RelayInfo remove(String id) throws IOException {
        RelayInfo old = relayInfos.remove(id);
        if (old == null) {
            return null;
        }
        timerService.removeAll(timerIds.get(id));
        persistenceService.storeRelays(relayInfos);
        return old;
    }

    /**
     * @param id
     */
    public void toggleRelay(String id) {
        gpioService.toggle(id);
    }

}
