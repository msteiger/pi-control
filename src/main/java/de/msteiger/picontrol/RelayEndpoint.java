package de.msteiger.picontrol;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.msteiger.picontrol.model.RelayInfo;
import de.msteiger.picontrol.services.RelayService;

@RestController
@RequestMapping(path = "relays", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class RelayEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(RelayEndpoint.class);

    private final RelayService dataService;

    @Autowired
    public RelayEndpoint(RelayService dataService) {
        this.dataService = dataService;
    }

    @RequestMapping
    public ResponseEntity<?> listAll() {
        logger.info("Listing all items");
        return ResponseEntity.ok(dataService.getAllRelays());
    }

    @RequestMapping("{id}/toggle")
    public ResponseEntity<?> toggle(@PathVariable("id") String id) {
        logger.info("Toggling relay with ID '{}'", id);
        dataService.toggleRelay(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping("{id}")
    public ResponseEntity<?> show(@PathVariable("id") String id) {
        logger.info("Listing details for ID '{}'", id);

        RelayInfo relayInfo = dataService.getRelay(id);
        if (relayInfo != null) {
            return ResponseEntity.ok(relayInfo);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @RequestMapping(path = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable("id") String id) throws IOException {
        logger.info("Deleting entries for ID '{}'", id);
        RelayInfo relayInfo = dataService.remove(id);
        if (relayInfo != null) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> update(@RequestBody RelayInfo info) throws IOException {
        logger.info("Updating entries for ID '{}'", info.getId());
        dataService.saveRelay(info);
        return ResponseEntity.ok().build();
    }
}
