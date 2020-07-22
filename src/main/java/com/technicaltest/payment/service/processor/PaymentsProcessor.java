package com.technicaltest.payment.service.processor;

import com.technicaltest.payment.service.client.LoggingClient;
import com.technicaltest.payment.service.client.PaymentValidator;
import com.technicaltest.payment.service.jdbi3.DatabaseWriter;
import com.technicaltest.payment.service.proto.Payments.Payment;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;

@Slf4j
@AllArgsConstructor(onConstructor = @__(@Inject))
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentsProcessor {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    DatabaseWriter databaseWriter;

    PaymentValidator paymentValidator;

    public void processOnlinePayment(Payment onlinePayment) throws IOException {
        logger.info("Online Payment: " + onlinePayment.toString());
        if (paymentValidator.isPaymentValid(onlinePayment)){
            logger.info("Valid online payment");
            databaseWriter.writePaymentToDatabase(onlinePayment);
        }
    }

    public void processOfflinePayment(Payment offlinePayment){
        logger.info("Offline Payment: " + offlinePayment.toString());
        databaseWriter.writePaymentToDatabase(offlinePayment);
    }

}
