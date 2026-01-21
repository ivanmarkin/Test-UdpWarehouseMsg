package net.ivm.lab.warehouse.central;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.ActorRef;

public class CentralServiceApp {
    public static final String ACTOR_SYSTEM = "WarehouseCentralActorSystem";
    public static final String CENTRAL_SERVICE_ACTOR = "centralServiceActor";

    public void start() {
        ActorSystem system = ActorSystem.create(ACTOR_SYSTEM);
        ActorRef centralService = system.actorOf(Props.create(CentralService.class), CENTRAL_SERVICE_ACTOR);
        centralService.tell(CentralService.START_CENTRAL_SERVICE, ActorRef.noSender());

        system.getWhenTerminated().toCompletableFuture().join();
    }

    public static void main(String[] args) {
        new CentralServiceApp().start();
    }
}
