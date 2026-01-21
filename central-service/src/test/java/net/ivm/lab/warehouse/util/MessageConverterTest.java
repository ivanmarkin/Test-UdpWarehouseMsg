package net.ivm.lab.warehouse.util;

import net.ivm.lab.warehouse.model.SensorData;
import net.ivm.lab.warehouse.model.SensorType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static net.ivm.lab.warehouse.model.SensorType.HUMIDITY;
import static net.ivm.lab.warehouse.model.SensorType.TEMPERATURE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MessageConverterTest {
    @Test
    public void testValidMessages() throws Exception {
        assertEquals(new SensorData("t1234", TEMPERATURE, 30d), MessageConverter.parseSensorMessage("  sensor_id =t1234; value=30 "));
        assertEquals(new SensorData("h33", HUMIDITY, 55.4), MessageConverter.parseSensorMessage("\tsensor_id =h33 ; value\t= 55.4 "));
        assertEquals(new SensorData("h", HUMIDITY, 41.99), MessageConverter.parseSensorMessage("\t value\t= 41.99 ; sensor_id =\th\t"));
    }

    @Test
    public void testInvalidMessages() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> MessageConverter.parseSensorMessage("sensor_id=p1234;value=30"));
        assertThrows(IllegalArgumentException.class, () -> MessageConverter.parseSensorMessage("sensor_id=1234;value=30"));
        assertThrows(IllegalArgumentException.class, () -> MessageConverter.parseSensorMessage("  sensor_id =t1234 value=30 "));
        assertThrows(NullPointerException.class, () -> MessageConverter.parseSensorMessage("  sensor_id =t1234; value= "));
        assertThrows(NumberFormatException.class, () -> MessageConverter.parseSensorMessage("  sensor_id =t1234; value=0.g"));
    }
}
