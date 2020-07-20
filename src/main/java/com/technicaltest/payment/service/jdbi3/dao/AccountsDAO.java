package com.technicaltest.payment.service.jdbi3.dao;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface AccountsDAO {
    @SqlUpdate("insert into accounts (id, name) values (:id, :name)")
    void insert(@Bind("id") int id, @Bind("name") String name);
}
