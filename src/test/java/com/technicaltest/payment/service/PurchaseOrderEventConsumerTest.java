package com.technicaltest.payment.service;

import com.technicaltest.payment.service.jdbi3.DatabaseWriter;
import com.technicaltest.payment.service.proto.Payments.Payment;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.MockConsumer;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.kafka.common.TopicPartition;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PurchaseOrderEventConsumerTest {

    MockConsumer<String, String> consumer;

    PurchaseOrderEventConsumer underTest;

    DatabaseWriter dbWriter;

    @Captor
    ArgumentCaptor<Payment> paymentCaptor;

    @BeforeEach
    void setUp() {
        paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        consumer = new MockConsumer<>(OffsetResetStrategy.EARLIEST);
        dbWriter = mock(DatabaseWriter.class);
        underTest =
                new PurchaseOrderEventConsumer(true, consumer, dbWriter);
    }

    @AfterEach
    public void tearDown() {
        consumer.close();
    }

    @Test
    public void givenPaymentEventWhenTransactionOfflineWriteToDatabase() {

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

        String topic = "offline";

        Collection<TopicPartition> partitions = new ArrayList<TopicPartition>();
        Collection<String> topicsCollection = new ArrayList<String>();
        partitions.add(new TopicPartition(topic, 1));
        Map<TopicPartition, Long> partitionsBeginningMap = new HashMap<TopicPartition, Long>();
        Map<TopicPartition, Long> partitionsEndMap = new HashMap<TopicPartition, Long>();

        long records = 10;
        for (TopicPartition partition : partitions) {
            partitionsBeginningMap.put(partition, 0l);
            partitionsEndMap.put(partition, records);
            topicsCollection.add(partition.topic());
        }

        consumer.subscribe(topicsCollection);
        consumer.rebalance(partitions);
        consumer.updateBeginningOffsets(partitionsBeginningMap);
        consumer.updateEndOffsets(partitionsEndMap);

        ConsumerRecord<String, String> record = new ConsumerRecord<String, String>(
                topic, 1, 0, null, messageValue);

        consumer.addRecord(record);

        consumer.schedulePollTask(() -> consumer.addRecord(record));
        consumer.schedulePollTask(() -> underTest.stop());

        underTest.startConsuming();

        verify(dbWriter).writePaymentToDatabase(paymentCaptor.capture());
        assertEquals(expected, paymentCaptor.getValue());
        assertEquals(true, consumer.closed());

    }

    @Test
    public void givenKafkaMessageValueReturnExpectedProtoPayment() throws ParseException {
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

        assertEquals(expected, actual);
    }
}