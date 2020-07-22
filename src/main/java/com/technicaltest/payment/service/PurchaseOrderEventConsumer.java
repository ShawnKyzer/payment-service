package com.technicaltest.payment.service;

import com.technicaltest.payment.service.processor.PaymentsProcessor;
import com.technicaltest.payment.service.proto.Payments.Payment;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Singleton
@AllArgsConstructor(onConstructor = @__(@Inject))
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseOrderEventConsumer {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    boolean kafkaAvailable;

    Consumer<String, String> currentConsumer;

    PaymentsProcessor paymentsProcessor;

    public void startConsuming() {
        Duration duration = Duration.ofSeconds(1);
        try {
            currentConsumer.subscribe(Arrays.asList("offline", "online"));
            while (true) {
                currentConsumer.poll(duration).forEach(message -> {
                            try {
                                // Adding the try catch here to avoid exiting loops
                                // and continuing to process
                                Payment curPayment = convertToPayment(message.value());
                                if (curPayment.getPaymentType().equals("offline")){
                                    paymentsProcessor.processOfflinePayment(curPayment);
                                } else if (curPayment.getPaymentType().equals("online")){
                                    paymentsProcessor.processOnlinePayment(curPayment);
                                }
                            } catch (Exception e) {
                                logger.error("Failed to write message to database", message.toString());
                            }
                        }
                );
                currentConsumer.commitAsync();
            }
        } catch (Exception exception) {
            logger.error("Failure to poll kafka:", exception);
            kafkaAvailable = false;
        } finally {
            currentConsumer.close();
        }
    }

    public boolean isKafkaAvailable() {
        return kafkaAvailable;
    }

    public Payment convertToPayment(String messageValue) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(messageValue);

        Payment convertedPayment = Payment.newBuilder()
                .setPaymentId((String) json.get("payment_id"))
                .setAccountId(((Long) json.get("account_id")).intValue())
                .setCreditCard(Optional.ofNullable(json.get("credit_card").toString()).orElse(""))
                .setPaymentType(json.get("payment_type").toString())
                .setAmount(((Long) json.get("amount")).intValue())
                .setDelay(((Long) json.get("delay")).intValue())
                .build();

        return convertedPayment;
    }

    public void stop() {
        kafkaAvailable = false;
        currentConsumer.wakeup();
    }
}
