package vttp.batch5.csf.assessment.server.repositories;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class OrdersRepository {

  @Autowired
  private MongoTemplate mongoTemplate;

  // Collection
  private final String C_MENUS = "menus";

  // Fields
  private final String F_NAME = "name";

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
  
}
