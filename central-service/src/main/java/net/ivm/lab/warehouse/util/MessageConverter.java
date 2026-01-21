package net.ivm.lab.warehouse.util;

import net.ivm.lab.warehouse.model.SensorData;
import net.ivm.lab.warehouse.model.SensorType;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class MessageConverter {
    private static final String PARAM_SENSOR_ID = "sensor_id";
    private static final String PARAM_SENSOR_VALUE = "value";

    public static SensorData parseSensorMessage(String message) {
        Map<String, String> result = Arrays.stream(message.replaceAll("\\s+", "").split(";"))
                .map(pair -> pair.split("=")).filter(kv -> kv.length == 2)
                .collect(Collectors.toMap(kv -> kv[0].toLowerCase(), kv -> kv[1]));

        String sensorId = result.get(PARAM_SENSOR_ID);
        if (StringUtils.isBlank(sensorId)) {
            throw new IllegalArgumentException(String.format("No %s in the message: %s", PARAM_SENSOR_ID, message));
        }

        char typeIndicator = sensorId.charAt(0);
        SensorType sensorType = switch (typeIndicator) {
            case 't' -> SensorType.TEMPERATURE;
            case 'h' -> SensorType.HUMIDITY;
            default -> throw new IllegalArgumentException("Invalid sensor type: " + typeIndicator);
        };

        double value = Double.parseDouble(result.get(PARAM_SENSOR_VALUE));

        return new SensorData(sensorId, sensorType, value);
    }

    public static String thresholdExceededAlert(SensorData sensorData) {
        return String.format("!!!Alarm: exceeded %s threshold of %s. Sensor ID=%s, value=%s",
                sensorData.sensorType().name(), sensorData.sensorType().getThreshold(), sensorData.sensorId(), sensorData.value());
    }
}
