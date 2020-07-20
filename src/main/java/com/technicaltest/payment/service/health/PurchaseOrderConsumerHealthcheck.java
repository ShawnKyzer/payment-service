package com.technicaltest.payment.service.health;

import com.codahale.metrics.health.HealthCheck;
import com.technicaltest.payment.service.PurchaseOrderConsumerConfiguration;
import com.technicaltest.payment.service.PurchaseOrderEventConsumer;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Environment;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;

@Slf4j
public class PurchaseOrderConsumerHealthcheck extends HealthCheck implements ConfiguredBundle<PurchaseOrderConsumerConfiguration> {

    private final PurchaseOrderEventConsumer kafkaConsumer;

    @Inject
    public PurchaseOrderConsumerHealthcheck(PurchaseOrderEventConsumer kafkaConsumer) {
        this.kafkaConsumer = kafkaConsumer;
    }

    @Override
    protected Result check() throws Exception {
        if (kafkaConsumer.isKafkaAvailable()){
           return Result.healthy();
        }
        return Result.unhealthy("Unable to reach kafka");
    }

    @Override
    public void run(PurchaseOrderConsumerConfiguration configuration, Environment environment) throws Exception {

    }
}
