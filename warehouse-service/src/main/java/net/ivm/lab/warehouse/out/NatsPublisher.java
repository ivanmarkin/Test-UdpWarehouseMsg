package net.ivm.lab.warehouse.out;

import io.nats.client.Connection;
import io.nats.client.JetStream;
import io.nats.client.Nats;
import io.nats.client.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class NatsPublisher {
    private static final Logger log = LoggerFactory.getLogger(NatsPublisher.class);

    private final Connection connection;
    private final JetStream jetStream;

    private final String messageTopic;

    public NatsPublisher(String messageBrokerUrl, String messageTopic) {
        try {
            this.connection = Nats.connect(new Options.Builder()
                    .server(messageBrokerUrl)
                    .connectionTimeout(Duration.ofSeconds(10))
                    .pingInterval(Duration.ofSeconds(2))
                    .maxReconnects(-1)  // Infinite reconnects
                    .reconnectWait(Duration.ofSeconds(2))
                    .build()
            );
            this.jetStream = connection.jetStream();
            this.messageTopic = messageTopic;
            log.info("Connected to msg broker at {}, messageTopic={}", messageBrokerUrl, messageTopic);
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to NATS: " + e.getMessage(), e);
        }
    }

    public void publish(String message) {
//            String json = objectMapper.writeValueAsString(data);
//            jetStream.publish(SensorData.MESSAGE_TOPIC, json.getBytes());
        jetStream.publishAsync(messageTopic, message.getBytes()).thenAccept(pubAck -> {
            System.out.println("Published successfully: " + pubAck);
        }).exceptionally(e -> {
            log.error("Publish failed: {}", e.getMessage());
            return null;
        });
    }
}