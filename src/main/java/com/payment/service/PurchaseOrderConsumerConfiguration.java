package com.payment.service;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.client.HttpClientConfiguration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.kafka.KafkaConsumerFactory;
import lombok.AllArgsConstructor;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@AllArgsConstructor(onConstructor = @__(@Inject))
public class PurchaseOrderConsumerConfiguration extends Configuration {
    @Valid
    @NotNull
    @JsonProperty("consumer")
    private KafkaConsumerFactory<String, String> kafkaConsumerFactory;

    public KafkaConsumerFactory<String, String> getKafkaConsumerFactory() {
        return kafkaConsumerFactory;
    }

    public void setKafkaConsumerFactory(KafkaConsumerFactory kafkaConsumerFactory) {
        this.kafkaConsumerFactory = kafkaConsumerFactory;
    }

    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory factory) {
        this.database = factory;
    }

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    @Valid
    @NotNull
    private HttpClientConfiguration httpClient = new HttpClientConfiguration();

    @JsonProperty("httpClient")
    public HttpClientConfiguration getHttpClientConfiguration() {
        return httpClient;
    }

    @JsonProperty("httpClient")
    public void setHttpClientConfiguration(HttpClientConfiguration httpClient) {
        this.httpClient = httpClient;
    }

}
