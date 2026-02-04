package net.ivm.lab.warehouse.central;

import net.ivm.lab.warehouse.model.ThresholdSettings;

public record MessageProcessorStart(
        ThresholdSettings thresholdSettings,
        MessageBrokerSettings messageBrokerSettings
) {
}
