package net.ivm.lab.warehouse.central;

public record MessageBrokerSettings (
        String brokerUrl,
        String sensorDataTopic,
        String sensorDataQueueGroup
) {
}
