package net.ivm.lab.warehouse.server;

public record Configuration(
        int portTemperature,
        int portHumidity,
        String messageBrokerUrl,
        String messageTopic
) {
}
