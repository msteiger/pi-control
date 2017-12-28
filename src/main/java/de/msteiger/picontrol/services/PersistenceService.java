package de.msteiger.picontrol.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import de.msteiger.picontrol.model.RelayInfo;

/**
 * TODO: describe
 */
@Component
public class PersistenceService {

    private static final Logger logger = LoggerFactory.getLogger(PersistenceService.class);

    private ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    @Value("${picontrol.configPath}")
    private String rootPath;

    /**
     * @param relaisInfos
     * @throws IOException
     */
    public void storeRelays(Map<String, RelayInfo> relaisInfos) throws IOException {
        Path relayFile = getRelayFile();
        relayFile.getParent().toFile().mkdirs();
        try (OutputStream os = Files.newOutputStream(relayFile)) {
            logger.debug("Writing to '{}'", relayFile);
            objectMapper.writeValue(os, relaisInfos);
        }
    }

    public Map<String, RelayInfo> loadRelays() throws IOException {
        if (!getRelayFile().toFile().exists()) {
            return new HashMap<>();
        }
        try (InputStream is = Files.newInputStream(getRelayFile())) {
            TypeReference<?> type = new TypeReference<Map<String, RelayInfo>>() { /**/ };
            return objectMapper.readValue(is, type);
        }
    }

    private Path getRelayFile() {
        return Paths.get(rootPath, "relays.json").normalize().toAbsolutePath();
    }
}