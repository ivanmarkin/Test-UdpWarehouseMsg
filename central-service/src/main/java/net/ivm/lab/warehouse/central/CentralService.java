package net.ivm.lab.warehouse.central;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.List;
import java.util.stream.IntStream;

import static net.ivm.lab.warehouse.central.MessageProcessor.START_MESSAGE_PROCESSOR;

public class CentralService extends AbstractActor {
    public static final String NATS_URL = "nats://localhost:4222";
    public static final String TOPIC_NAME = "WarehouseSensorDataTopic";
    public static final String QUEUE_GROUP = "WarehouseSensorDataQueueGroup";

    public static final double TH_TEMPERATURE = 35.0;
    public static final double TH_HUMIDITY = 50.0;

    static final String START_CENTRAL_SERVICE = "StartCentralService";
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    List<ActorRef> messageProcessors;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals(START_CENTRAL_SERVICE, msg -> start())
                .build();
    }

    private void start() {
        if (messageProcessors == null) {
            log.info("Starting CentralService...");
            messageProcessors = IntStream.rangeClosed(1, 3)
                    .mapToObj(i -> getContext().actorOf(Props.create(MessageProcessor.class)))
                    .peek(messageProcessor -> messageProcessor.tell(START_MESSAGE_PROCESSOR, getSelf()))
                    .toList();
            log.info("{} MessageProcessors running", messageProcessors.size());
        } else {
            log.warning("CentralService is already started: {} MessageProcessors running", messageProcessors.size());
        }
    }
}
