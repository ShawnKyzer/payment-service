package com.payment.service.jdbi3.dao;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface AccountsDAO {
    @SqlUpdate ("UPDATE accounts " +
            "SET last_payment_date = (select created_on from payments where payment_id = :payment_id) " +
            "WHERE account_id = (select account_id from payments where payment_id = :payment_id)")
    void updateLastPaymentDate(@Bind("payment_id") String payment_id);
}
