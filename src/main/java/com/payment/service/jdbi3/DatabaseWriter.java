package com.payment.service.jdbi3;

import com.payment.service.jdbi3.dao.AccountsDAO;
import com.payment.service.jdbi3.dao.PaymentsDAO;
import com.payment.service.proto.Payments.Payment;
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
        PaymentsDAO paymentsDAOao = jdbi.onDemand(PaymentsDAO.class);
        paymentsDAOao.insert(paymentToWrite);

        AccountsDAO accountsDAO = jdbi.onDemand(AccountsDAO.class);
        accountsDAO.updateLastPaymentDate(paymentToWrite.getPaymentId());
    }

}
