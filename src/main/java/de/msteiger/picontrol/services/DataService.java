package de.msteiger.picontrol.services;

import java.io.IOException;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.msteiger.picontrol.model.RelayInfo;

/**
 * TODO: describe
 */
@Component
public class DataService {

    @Autowired
    private final PersistenceService persistenceService = new PersistenceService();

    private Map<String, RelayInfo> relaisInfos;

    @PostConstruct
    private void init() throws IOException {
        relaisInfos = persistenceService.loadRelays();
    }

    /**
     * @param id
     * @param ri
     */
    public void saveRelay(String id, RelayInfo ri) throws IOException {
        relaisInfos.put(id, ri);
        persistenceService.storeRelays(relaisInfos);
    }

    /**
     * @param id
     * @return
     */
    public RelayInfo getRelay(String id) {
        return relaisInfos.get(id);
    }

    /**
     * @param id
     * @return
     */
    public RelayInfo remove(String id) throws IOException {
        RelayInfo old = relaisInfos.remove(id);
        if (old != null) {
            persistenceService.storeRelays(relaisInfos);
        }
        return old;
    }

}
