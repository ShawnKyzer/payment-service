package com.technicaltest.payment.service.jdbi3.dao;

import com.technicaltest.payment.service.proto.Payments.Payment;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface PaymentsDAO {
    @SqlUpdate("insert into payments (" +
            "payment_id, " +
            "account_id, " +
            "payment_type, " +
            "credit_card, " +
            "amount, " +
            "created_on) values " +
            "(:paymentId_, :accountId_, :paymentType_, :creditCard_, :amount_)")
    void insert(@BindBean Payment payment);
}
