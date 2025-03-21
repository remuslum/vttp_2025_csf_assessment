package vttp.batch5.csf.assessment.server.services;

import java.io.StringReader;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import vttp.batch5.csf.assessment.server.repositories.OrdersRepository;
import vttp.batch5.csf.assessment.server.repositories.PaymentRepository;

@Service
public class RestaurantService {

  @Autowired
  private OrdersRepository ordersRepository;

  @Autowired
  private PaymentRepository paymentRepository;

  // TODO: Task 2.2
  // You may change the method's signature
  public JsonArray getMenu() {
    List<Document> menuItems = ordersRepository.getMenu();
    JsonArrayBuilder menuArrayBuilder = Json.createArrayBuilder();

    // Map to jsonobject then add to jsonarray
    menuItems.forEach(m -> {
      JsonObject item = Json.createReader(new StringReader(m.toJson())).readObject();
      menuArrayBuilder.add(item);
    });
    return menuArrayBuilder.build();
  }
  
  // TODO: Task 4
  public ResponseEntity<String> sendPayment(String payload){
    JsonObject object = Json.createReader(new StringReader(payload)).readObject();
    return paymentRepository.sendOrder(object.getString("username"), 10.00);
  }

}
