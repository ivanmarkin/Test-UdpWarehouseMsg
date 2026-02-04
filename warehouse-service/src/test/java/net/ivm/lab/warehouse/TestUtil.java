package net.ivm.lab.warehouse;

import net.ivm.lab.warehouse.server.Configuration;
import net.ivm.lab.warehouse.server.ConfigurationFactory;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class TestUtil {
    private static final Configuration defaultLocalConfiguration = ConfigurationFactory.defaultLocalhost();

    public static Configuration testConfiguration() {
        return new Configuration(10444, 10445, defaultLocalConfiguration.messageBrokerUrl(), "RealIntegrationTestWarehouseTopic");
    }

    public static void sendUdpMessage(String host, int port, String message) throws IOException {
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            byte[] sendData = message.getBytes(StandardCharsets.UTF_8);
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(host), port);
            clientSocket.send(sendPacket);
        }
    }
}
