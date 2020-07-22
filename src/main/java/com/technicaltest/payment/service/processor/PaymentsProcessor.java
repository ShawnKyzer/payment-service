package com.technicaltest.payment.service.processor;

import com.technicaltest.payment.service.client.LoggingClient;
import com.technicaltest.payment.service.jdbi3.DatabaseWriter;
import com.technicaltest.payment.service.proto.Payments.Payment;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@Slf4j
@AllArgsConstructor(onConstructor = @__(@Inject))
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentsProcessor {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    DatabaseWriter databaseWriter;

    final LoggingClient loggingClient = new LoggingClient();

    public void processOnlinePayment(Payment onlinePayment){
        logger.info("Online Payment: " + onlinePayment.toString());
        databaseWriter.writePaymentToDatabase(onlinePayment);
    }

    public void processOfflinePayment(Payment offlinePayment){
        logger.info("Offline Payment: " + offlinePayment.toString());
        databaseWriter.writePaymentToDatabase(offlinePayment);
    }

}
