package net.ivm.lab.warehouse.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SensorDataTest {
    @Test
    public void testSensorDataToString() {
        String sensorId = "1234";
        SensorType sensorType = SensorType.TEMPERATURE;
        double data = 34.5;
        Assertions.assertEquals(new SensorData(sensorId, sensorType, data).toString(),
                String.format("SensorData[sensorId=%s, sensorType=%s, value=%s]", sensorId, sensorType, data));
    }
}
