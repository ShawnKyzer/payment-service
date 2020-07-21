package com.technicaltest.payment.service;

import com.technicaltest.payment.service.jdbi3.DatabaseWriter;
import com.technicaltest.payment.service.proto.Payments.Payment;
import org.apache.kafka.clients.consumer.Consumer;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class PurchaseOrderEventConsumerTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void startConsuming() {
    }

    @Test
    public void isKafkaAvailable() {
    }

    @Test
    public void convertToPayment() throws ParseException {
        Consumer<String,String> mockedConsumer = mock(Consumer.class);
        boolean isKafkaAvailable = true;
        PurchaseOrderEventConsumer underTest =
                new PurchaseOrderEventConsumer(isKafkaAvailable, mockedConsumer, mock(DatabaseWriter.class));

        String messageValue = "{\"payment_id\": \"20680a5d-2f0e-4d8d-9910-bd8a455c2df7\", " +
                "\"account_id\": 468, " +
                "\"payment_type\":\"offline\", " +
                "\"credit_card\": \"\", " +
                "\"amount\": 32, " +
                "\"delay\": 221}";

        Payment expected = Payment.newBuilder()
                .setPaymentId("20680a5d-2f0e-4d8d-9910-bd8a455c2df7")
                .setAccountId(468)
                .setCreditCard("")
                .setPaymentType("offline")
                .setAmount(32)
                .setDelay(221)
                .build();

        Payment actual = underTest.convertToPayment(messageValue);

        assertEquals(expected,actual);
    }
}