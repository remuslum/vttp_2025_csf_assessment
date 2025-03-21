package vttp.batch5.csf.assessment.server.repositories;

import java.util.LinkedList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import static vttp.batch5.csf.assessment.server.util.DocumentFields.ORDER_ID;

@Repository
public class OrdersRepository {

  @Autowired
  private MongoTemplate mongoTemplate;

  // Collection
  private final String C_MENUS = "menus";
  private final String C_ORDERS = "orders";

  // Fields
  private final String F_NAME = "name";

  // Fields for orders
  private final String F_ID = "_id";
  private final String F_PRICE = "price";
  private final String F_QUANTITY = "quantity";
  private final String F_ITEMS = "items";

  // TODO: Task 2.2
  // You may change the method's signature
  // Write the native MongoDB query in the comment below
  //
  //  Native MongoDB query here
  //  db.menus.find().sort({name:1})
  public List<Document> getMenu() {
    Query query = new Query();
    query.with(Sort.by(Sort.Direction.ASC, F_NAME));
    return mongoTemplate.find(query, Document.class, C_MENUS);
  }

  // TODO: Task 4
  // Write the native MongoDB query for your access methods in the comment below
  //
  //  Native MongoDB query here
  public boolean insertOrder(Document toInsert, JsonArray items){
    // convert items to a list of documents
    List<Document> itemDocuments = new LinkedList<>();
    for(int i = 0; i < items.size(); i++){
      JsonObject object = items.getJsonObject(i);
      Document d = new Document();
      d.append(F_ID,object.getString("_id")).append(F_PRICE, object.getJsonNumber("price").doubleValue())
      .append(F_QUANTITY, object.getInt("quantity"));
      itemDocuments.add(d);
    }

    toInsert.append(F_ID,toInsert.getString(ORDER_ID)).append(F_ITEMS,itemDocuments);
    Document doc = mongoTemplate.insert(toInsert, C_ORDERS);
    // Check if the returned doc has a _id
    return !doc.getString("_id").isBlank();

  }
}
