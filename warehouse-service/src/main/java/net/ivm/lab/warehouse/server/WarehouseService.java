package net.ivm.lab.warehouse.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WarehouseService {
    private static final Logger log = LoggerFactory.getLogger(WarehouseService.class);

    private final ServerContext serverContext;

    public WarehouseService(Configuration configuration) {
        this.serverContext = new ServerContext(configuration);
    }

    public void start() {
        serverContext.udpTemperatureListener().start();
        serverContext.udpHumidityListener().start();

        log.info("WarehouseService started");

        try {
            Thread.currentThread().join();
        } catch (InterruptedException ignored) {
        }
    }
}