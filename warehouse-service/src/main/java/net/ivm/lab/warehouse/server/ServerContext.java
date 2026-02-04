package net.ivm.lab.warehouse.server;

import net.ivm.lab.warehouse.in.UdpListener;
import net.ivm.lab.warehouse.out.NatsPublisher;

public class ServerContext {
    private final UdpListener temperatureListener;
    private final UdpListener humidityListener;

    public ServerContext(Configuration configuration) {
        NatsPublisher natsPublisher = new NatsPublisher(configuration.messageBrokerUrl(), configuration.messageTopic());
        this.temperatureListener = new UdpListener(configuration.portTemperature(), natsPublisher);
        this.humidityListener = new UdpListener(configuration.portHumidity(), natsPublisher);
    }

    public UdpListener udpTemperatureListener() {
        return temperatureListener;
    }

    public UdpListener udpHumidityListener() {
        return humidityListener;
    }
}
