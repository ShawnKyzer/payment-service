package com.technicaltest.payment.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Duration;
import java.util.Collections;

@Slf4j
@Singleton
@AllArgsConstructor(onConstructor = @__(@Inject))
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseOrderEventConsumer {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    boolean kafkaAvailable;

    Consumer<String, String> currentConsumer;

    public void startConsuming() {
        Duration duration = Duration.ofSeconds(1);
        try {
            currentConsumer.subscribe(Collections.singleton("offline"));
            while (true) {
                currentConsumer.poll(duration).forEach(message -> {
                            logger.info("Message: " + message.toString());
                        }
                );
                currentConsumer.commitAsync();
            }
        } catch (Exception exception){
            logger.error("Failure to poll kafka:", exception);
            kafkaAvailable = false;
        }
    }

    public boolean isKafkaAvailable() {
        return kafkaAvailable;
    }
}
