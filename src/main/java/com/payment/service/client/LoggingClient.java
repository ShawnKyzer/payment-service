package com.payment.service.client;

import com.technicaltest.payment.service.proto.Payments.Payment;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.simple.JSONObject;

import javax.inject.Inject;
import java.io.IOException;

@Slf4j
@AllArgsConstructor(onConstructor = @__(@Inject))
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoggingClient {

    HttpClient httpClient;

    public void logError(Payment payment, String errorType, String errorDescription) throws IOException {

        String logErrorHostURL = System.getenv("LOGHOST");
        if (logErrorHostURL == null){
            logErrorHostURL = "http://localhost:9000/log";
        }

        HttpPost httpPost = new HttpPost(logErrorHostURL);

        JSONObject payload = new JSONObject();
        payload.put("payment_id", payment.getPaymentId());
        payload.put("error_type",errorType);
        payload.put("error_description",errorDescription);

        String json = payload.toString();
        StringEntity entity = new StringEntity(json);

        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        // Note that we have the drop wizard config to determine the retries behind the scene
        HttpResponse response = httpClient.execute(httpPost);
    }

}
