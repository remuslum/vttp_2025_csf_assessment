package vttp.batch5.csf.assessment.server.repositories;

import java.util.UUID;

import org.bson.Document;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class PaymentRepository {
    
    private final String PAYMENT_URL = "https://payment-service-production-a75a.up.railway.app/api/payment";

    private final String PAYEE = "Remus";

    private final String F_ORDER_ID = "order_id";
    private final String F_PAYER = "payer";
    private final String F_PAYEE = "payee";
    private final String F_PAYMENT = "payment";

    public ResponseEntity<String> sendOrder(String username, double amount){
        // Initate payload
        String orderId = UUID.randomUUID().toString().substring(0,8);
        Document payload = new Document();
        payload.append(F_ORDER_ID, orderId).append(F_PAYER, username).append(F_PAYEE, PAYEE).append(F_PAYMENT,amount);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Authenticate",username);
        //Build request
        RequestEntity<String> request = RequestEntity.post(PAYMENT_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .headers(headers)
        .body(payload.toJson(), String.class);

        // Send request
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(request, String.class);

    }
}
