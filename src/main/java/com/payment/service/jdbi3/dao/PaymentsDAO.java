package com.payment.service.jdbi3.dao;

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
            "(:paymentId, :accountId, :paymentType, :creditCard, :amount, now())")
    void insert(@BindBean Payment payment);
}
