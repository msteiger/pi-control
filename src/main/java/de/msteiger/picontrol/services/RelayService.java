package de.msteiger.picontrol.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    private Map<String, RelayInfo> relayInfos = new LinkedHashMap<>();
    private Map<String, List<String>> timerIds = new LinkedHashMap<>();

    @PostConstruct
    private void init() throws IOException {
        List<RelayInfo> list = persistenceService.loadRelays();
        for (RelayInfo info : list) {
            relayInfos.put(info.getId(), info);
            updateSchedules(info.getId(), info);
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
            if (!tt.getWeekDays().isEmpty()) {
                String timerId = timerService.schedule(tt.getWeekDays(), tt.getTime(), () -> toggleRelay(id));
                timerIdList.add(timerId);
                logger.debug("Scheduled timer '{}' at '{}'", id, tt);
            }
        }
    }

    /**
     * @param id
     * @param ri
     * @throws IOException
     */
    public void saveRelay(RelayInfo ri) throws IOException {
        relayInfos.put(ri.getId(), ri);
        persistenceService.storeRelays(relayInfos.values());
        updateSchedules(ri.getId(), ri);
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
     * @throws IOException
     */
    public RelayInfo remove(String id) throws IOException {
        RelayInfo old = relayInfos.remove(id);
        if (old == null) {
            return null;
        }
        timerService.removeAll(timerIds.get(id));
        persistenceService.storeRelays(relayInfos.values());
        return old;
    }

    /**
     * @param id
     */
    public void toggleRelay(String id) {
        RelayInfo relayInfo = relayInfos.get(id);
        if (relayInfo != null) {
            gpioService.toggle(relayInfo.getGpioPin());
        } else {
            logger.warn("No relay with ID '{}' found", id);
        }
    }

    /**
     * @return
     */
    public Collection<RelayInfo> getAllRelays() {
        return relayInfos.values();
    }

}
