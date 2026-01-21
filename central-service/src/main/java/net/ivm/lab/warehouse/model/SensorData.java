package net.ivm.lab.warehouse.model;

public record SensorData(
        String sensorId,
        SensorType sensorType,
        double value
) {
    public boolean exceedsThreshold() {
        return value > sensorType().getThreshold();
    }
}