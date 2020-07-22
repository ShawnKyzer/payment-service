package com.technicaltest.payment.service.client;

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
public class PaymentValidator {

    HttpClient httpClient;

    public boolean isPaymentValid(Payment paymentToValidate) throws IOException {

        HttpPost httpPost = new HttpPost("http://localhost:9000/payment");

        JSONObject payload = new JSONObject();
        payload.put("payment_id",paymentToValidate.getPaymentId());
        payload.put("account_id",paymentToValidate.getAccountId());
        payload.put("payment_type",paymentToValidate.getPaymentType());
        payload.put("credit_card",paymentToValidate.getCreditCard());
        payload.put("amount",paymentToValidate.getAmount());

        String json = payload.toString();
        StringEntity entity = new StringEntity(json);

        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        HttpResponse response = httpClient.execute(httpPost);

        if (response.getStatusLine().getStatusCode() == 200) {
            return true;
        }

        return false;
    }

}
