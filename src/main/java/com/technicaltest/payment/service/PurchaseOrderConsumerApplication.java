package com.technicaltest.payment.service;

import com.technicaltest.payment.service.client.LoggingClient;
import com.technicaltest.payment.service.client.PaymentValidator;
import com.technicaltest.payment.service.jdbi3.DatabaseWriter;
import com.technicaltest.payment.service.processor.PaymentsProcessor;
import io.dropwizard.Application;
import io.dropwizard.client.HttpClientBuilder;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.kafka.KafkaConsumerBundle;
import io.dropwizard.kafka.KafkaConsumerFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.AllArgsConstructor;
import org.apache.http.client.HttpClient;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;
import org.jdbi.v3.core.Jdbi;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor(onConstructor = @__(@Inject))
public class PurchaseOrderConsumerApplication extends Application<PurchaseOrderConsumerConfiguration> {

    public static void main(final String[] args) throws Exception {
        new PurchaseOrderConsumerApplication().run(args);
    }

    @Inject
    private final KafkaConsumerBundle<String, String, PurchaseOrderConsumerConfiguration> kafkaConsumer =
            new KafkaConsumerBundle<String, String, PurchaseOrderConsumerConfiguration>(List.of("offline"),
                    new ConsumerRebalanceListener() {
                        @Override
                        public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                            // do nothing
                        }

                        @Override
                        public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                            // do nothing
                        }
                    }) {
                @Override
                public KafkaConsumerFactory<String, String> getKafkaConsumerFactory(PurchaseOrderConsumerConfiguration configuration) {
                    return configuration.getKafkaConsumerFactory();
                }
            };

    @Override
    public void initialize(Bootstrap<PurchaseOrderConsumerConfiguration> bootstrap) {
        bootstrap.addBundle(kafkaConsumer);
    }

    @Override
    public void run(PurchaseOrderConsumerConfiguration config, Environment environment) {
        final HttpClient httpClient = new HttpClientBuilder(environment).using(config.getHttpClientConfiguration())
                .build(getName());
        environment.jersey().register(httpClient);

        PaymentValidator paymentValidator = new PaymentValidator(httpClient);
        LoggingClient loggingClient = new LoggingClient(httpClient);

        final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(
                environment,
                config.getDataSourceFactory()
                , "postgresql");
        DatabaseWriter databaseWriter = new DatabaseWriter(jdbi);
        PaymentsProcessor paymentsProcessor = new PaymentsProcessor(databaseWriter, paymentValidator);

        // This allows for all of the out of the box goodies
        environment.jersey().register(databaseWriter);

        PurchaseOrderEventConsumer purchaseOrderEventConsumer =
                new PurchaseOrderEventConsumer(
                        true,
                        kafkaConsumer.getConsumer(),
                        paymentsProcessor,
                        loggingClient);

        purchaseOrderEventConsumer.startConsuming();
    }

}
