package net.ivm.lab.warehouse.model;

import net.ivm.lab.warehouse.central.CentralService;

public enum SensorType {
    TEMPERATURE(CentralService.TH_TEMPERATURE),
    HUMIDITY(CentralService.TH_HUMIDITY);

    private final double threshold;

    SensorType(double threshold) {
        this.threshold = threshold;
    }

    public double getThreshold() {
        return threshold;
    }
}
