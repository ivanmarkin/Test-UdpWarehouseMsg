package net.ivm.lab.warehouse;

import net.ivm.lab.warehouse.server.ConfigurationFactory;
import net.ivm.lab.warehouse.server.WarehouseService;

public class WarehouseServiceApp {
    public static void main(String[] args) {
        new WarehouseService(ConfigurationFactory.localhost()).start();
    }
}
