package net.ivm.lab.warehouse.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

public class ConfigurationFactory {
    private static final Logger log = LoggerFactory.getLogger(ConfigurationFactory.class);

    private static final String PROPS_LOCALHOST = "application.localhost.properties";

    private static final String PR_MESSAGING_BROKER_URL = "messaging.broker.url";
    private static final String PR_SENSOR_DATA_TOPIC = "messaging.broker.sensor-data.topic";
    private static final String PR_SENSOR_DATA_PORT_TEMPERATURE = "sensor-data.port.temperature";
    private static final String PR_SENSOR_DATA_PORT_HUMIDITY = "sensor-data.port.humidity";

    private static final Map<String, Configuration> configs = new HashMap<>();

    static {
        configs.put(PROPS_LOCALHOST, Optional.ofNullable(loadFromFile(PROPS_LOCALHOST)).orElse(defaultLocalhost()));
    }

    public static Configuration localhost() {
        return configs.get(PROPS_LOCALHOST);
    }

    public static Configuration defaultLocalhost() {
        return new Configuration(13344, 13355, "nats://localhost:4222", "WarehouseSensorDataTopic");
    }

    private static Configuration loadFromFile(String propsFile) {
        try {
            Properties props = new Properties();
            InputStreamReader reader = new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(propsFile), StandardCharsets.UTF_8);
            props.load(reader);

            return new Configuration(
                    Integer.parseInt(props.getProperty(PR_SENSOR_DATA_PORT_TEMPERATURE)),
                    Integer.parseInt(props.getProperty(PR_SENSOR_DATA_PORT_HUMIDITY)),
                    props.getProperty(PR_MESSAGING_BROKER_URL),
                    props.getProperty(PR_SENSOR_DATA_TOPIC)
            );
        } catch (Exception ex) {
            log.error("Error reading properties file {}", propsFile, ex);
            return null;
        }
    }
}
