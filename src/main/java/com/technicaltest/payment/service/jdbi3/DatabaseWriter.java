package com.technicaltest.payment.service.jdbi3;

import com.technicaltest.payment.service.jdbi3.dao.PaymentsDAO;
import com.technicaltest.payment.service.proto.Payments.Payment;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;

import javax.inject.Inject;
import javax.inject.Singleton;

@Slf4j
@Singleton
@AllArgsConstructor(onConstructor = @__(@Inject))
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DatabaseWriter {

    @Inject
    Jdbi jdbi;

    public void writePaymentToDatabase(Payment paymentToWrite){
        PaymentsDAO dao = jdbi.onDemand(PaymentsDAO.class);
        dao.insert(paymentToWrite);
    }

}
