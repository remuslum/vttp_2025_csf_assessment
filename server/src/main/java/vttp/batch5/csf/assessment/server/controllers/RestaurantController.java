package vttp.batch5.csf.assessment.server.controllers;

import java.io.StringReader;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import vttp.batch5.csf.assessment.server.repositories.RestaurantRepository;
import vttp.batch5.csf.assessment.server.services.RestaurantService;

@RestController
@RequestMapping(path="/api", produces=MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {

  @Autowired
  private RestaurantService restaurantService;

  @Autowired
  private RestaurantRepository restaurantRepository;

  // TODO: Task 2.2
  // You may change the method's signature
  @GetMapping(path="/menu")
  public ResponseEntity<String> getMenus() {
    return ResponseEntity.ok(restaurantService.getMenu().toString());
  }

  // TODO: Task 4
  // Do not change the method's signature
  @PostMapping(path="/food_order")
  public ResponseEntity<String> postFoodOrder(@RequestBody String payload) {
    System.out.println(payload);
    JsonObject object = Json.createReader(new StringReader(payload)).readObject();
    String username = object.getString("username");
    String password = object.getString("password");
    JsonArray items = object.getJsonArray("items");
    double totalPrice = calculatePrice(items);
    
    if(!restaurantRepository.isUserValid(username, password)){
      Document error = new Document().append("message", "Invalid username and/or password");
      return new ResponseEntity<>(error.toJson(), HttpStatus.UNAUTHORIZED);
    } else {
      try {
          ResponseEntity<String> paymentResponse = restaurantService.sendPayment(username, totalPrice);
          JsonObject response = Json.createReader(new StringReader(paymentResponse.getBody())).readObject();

          if(restaurantService.saveData(response, username, items)){
            Document returnMessage = new Document();
            returnMessage.append("orderId",response.getString("order_id")).append("paymentId",response.getString("payment_id"))
            .append("total",response.getJsonNumber("total").doubleValue()).append("timestamp",response.getJsonNumber("timestamp").longValue());
            return ResponseEntity.ok(returnMessage.toJson());
          } else {
            Document errorMessage = new Document().append("message","Unable to insert into db");
            return ResponseEntity.badRequest().body(errorMessage.toJson());
          }
      } catch (Exception e) {
        Document errorMessage = new Document().append("message",e.getMessage());
        return new ResponseEntity<>(errorMessage.toJson(), HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
  }

  private double calculatePrice(JsonArray array){
    double amount = 0.00;
    for(int i = 0; i < array.size(); i++){
      JsonObject object = array.getJsonObject(i);
      amount += (object.getJsonNumber("price").doubleValue() * object.getInt("quantity"));
    }
    return amount;
  }
}
