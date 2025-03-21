package vttp.batch5.csf.assessment.server.services;

import java.io.StringReader;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import vttp.batch5.csf.assessment.server.repositories.OrdersRepository;
import vttp.batch5.csf.assessment.server.repositories.PaymentRepository;
import vttp.batch5.csf.assessment.server.repositories.RestaurantRepository;
import static vttp.batch5.csf.assessment.server.util.DocumentFields.ORDER_DATE;
import static vttp.batch5.csf.assessment.server.util.DocumentFields.ORDER_ID;
import static vttp.batch5.csf.assessment.server.util.DocumentFields.PAYMENT_ID;
import static vttp.batch5.csf.assessment.server.util.DocumentFields.TOTAL;
import static vttp.batch5.csf.assessment.server.util.DocumentFields.USERNAME;

@Service
public class RestaurantService {

  @Autowired
  private OrdersRepository ordersRepository;

  @Autowired
  private PaymentRepository paymentRepository;

  @Autowired
  private RestaurantRepository restaurantRepository;

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
  @Transactional
  public boolean saveData(JsonObject response, String username, JsonArray items){
    long dateInMilli = response.getJsonNumber("timestamp").longValue();
    // Conversion
    LocalDate date = Instant.ofEpochMilli(dateInMilli).atZone(ZoneId.systemDefault()).toLocalDate();
    double total = response.getJsonNumber("total").bigDecimalValue().setScale(2, RoundingMode.HALF_UP).doubleValue();

    Document d = new Document();
    d.append(ORDER_ID, response.getString("order_id")).append(PAYMENT_ID,response.getString("payment_id"))
    .append(ORDER_DATE, date.toString()).append(TOTAL,total).append(USERNAME,username);


    return restaurantRepository.addOrderDetails(username, d) && ordersRepository.insertOrder(d, items);
  }

  public ResponseEntity<String> sendPayment(String username, double amount){
    return paymentRepository.sendOrder(username, amount);
  }

}
