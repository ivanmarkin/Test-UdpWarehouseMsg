package net.ivm.lab.warehouse.central;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Message;
import io.nats.client.Nats;
import net.ivm.lab.warehouse.model.SensorData;
import net.ivm.lab.warehouse.model.ThresholdSettings;
import net.ivm.lab.warehouse.util.MessageConverter;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;

public class MessageProcessor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private final AtomicBoolean started = new AtomicBoolean(false);
    private ThresholdSettings thresholdSettings;
    private MessageBrokerSettings mbs;

    private Connection natsConnection;
    private Dispatcher natsDispatcher;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(MessageProcessorStart.class, this::start)
                .build();
    }

    private void start(MessageProcessorStart msg) {
        if (started.get()) {
            log.warning("MessageProcessor {} already started", this);
            return;
        }

        try {
            thresholdSettings = msg.thresholdSettings();
            mbs = msg.messageBrokerSettings();

            natsConnection = Nats.connect(mbs.brokerUrl());
            natsDispatcher = natsConnection.createDispatcher(this::processNatsMessage);
            natsDispatcher.subscribe(mbs.sensorDataTopic(), mbs.sensorDataQueueGroup());

            started.set(true);
            log.info("MessageProcessor {} started", this);
        } catch (Exception e) {
            log.error("Failed to connect to NATS: {}", e);
            getContext().stop(getSelf());
        }
    }

    private void processNatsMessage(Message msg) {
        String content = new String(msg.getData(), StandardCharsets.UTF_8);
        try {
            SensorData sensorData = MessageConverter.parseSensorMessage(content);
            double threshold = thresholdSettings.getThresholdFor(sensorData.sensorType());
            if (sensorData.value() > threshold) {
                log.error(String.format("!!!Alarm: exceeded %s threshold of %s. Sensor ID=%s, value=%s",
                        sensorData.sensorType().name(), threshold, sensorData.sensorId(), sensorData.value()));
            }
        } catch (Exception e) {
            log.error("Failed to parse sensor message: {}\n {}", content, e);
        }
    }

    @Override
    public void postStop() {
        clearConnection();
    }

    private void clearConnection() {
        try {
            if (natsDispatcher != null) {
                natsDispatcher.unsubscribe(mbs.sensorDataTopic());
            }
            if (natsConnection != null) {
                natsConnection.close();
            }
        } catch (Exception e) {
            log.error("Error clearing NATS connection: {}", e);
        }
    }
}