package net.ivm.lab.warehouse;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Nats;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTest {
    private static final Logger log = LoggerFactory.getLogger(IntegrationTest.class);
    private static final Configuration configuration = TestUtil.testConfiguration();

    private Thread serviceThread;
    private Connection connection;

    @BeforeEach
    public void startServices() throws Exception {
        serviceThread = Thread.ofVirtual().start(() -> new WarehouseService(configuration).start());
        connection = Nats.connect(configuration.messageBrokerUrl());

        Thread.sleep(1000);
    }

    @AfterEach
    public void stopServices() throws Exception {
        serviceThread.interrupt();
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void testMessagesAsync() throws Exception {
        int msgCount = 20;
        CountDownLatch messagesLatch = new CountDownLatch(msgCount);
        List<String> receivedMessages = new ArrayList<>();

        Dispatcher dispatcher = connection.createDispatcher((msg) -> {
            receivedMessages.add(new String(msg.getData(), StandardCharsets.UTF_8));
            messagesLatch.countDown();
        });
        dispatcher.subscribe(configuration.messageTopic());

        List<String> sentMessages = new ArrayList<>();
        Random random = new Random();
        boolean terminated;
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < msgCount; i++) {
                String message = "sensor_id=" + i + "; value=" + i;
                sentMessages.add(message);
                int port = random.nextBoolean() ? configuration.portTemperature() : configuration.portHumidity();

                executor.submit(() -> {
                    try {
                        TestUtil.sendUdpMessage("localhost", port, message);
                        Thread.sleep(50);
                    } catch (Exception e) {
                        fail("Failed to send UDP: " + e.getMessage());
                    }
                });
            }

            executor.shutdown();
            terminated = executor.awaitTermination(1, TimeUnit.SECONDS);
        }
        assertTrue(terminated, "terminated = false");

        boolean allReceived = messagesLatch.await(msgCount * 2, TimeUnit.SECONDS);
        assertTrue(allReceived, "allReceived = false");

        assertEquals(sentMessages.size(), receivedMessages.size());
        log.info("sentMessages.equals(receivedMessages) = {}", sentMessages.equals(receivedMessages));

        assertTrue(sentMessages.containsAll(receivedMessages));
    }
}