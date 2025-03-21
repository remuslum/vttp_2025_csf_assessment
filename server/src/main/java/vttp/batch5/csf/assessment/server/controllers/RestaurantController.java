package vttp.batch5.csf.assessment.server.controllers;

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
    // {"username":"fred","password":"fred",
    // "items":[{"_id":"9aedc2a8","name":"Balik Ekmek","description":"BALIK EKMEK / ISTANBUL STREET FLAVOR, FISH SANDWICH","price":9.2,"quantity":1},{"_id":"b9f0f5e1","name":"Chicken Bruschetta","description":"Crunch, succulent juicy chicken and a glaze drizzle","price":7.7,"quantity":1}]}
    if(!restaurantRepository.isUserValid(payload)){
      Document error = new Document().append("message", "Invalid username and/or password");
      return new ResponseEntity<>(error.toJson(), HttpStatus.UNAUTHORIZED);
    } else {
      return restaurantService.sendPayment(payload);
    }
  }
}
