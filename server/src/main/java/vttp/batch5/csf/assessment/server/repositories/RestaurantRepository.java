package vttp.batch5.csf.assessment.server.repositories;

import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import static vttp.batch5.csf.assessment.server.util.DocumentFields.ORDER_DATE;
import static vttp.batch5.csf.assessment.server.util.DocumentFields.ORDER_ID;
import static vttp.batch5.csf.assessment.server.util.DocumentFields.PAYMENT_ID;
import static vttp.batch5.csf.assessment.server.util.DocumentFields.TOTAL;
import static vttp.batch5.csf.assessment.server.util.DocumentFields.USERNAME;

// Use the following class for MySQL database
@Repository
public class RestaurantRepository {

    @Autowired
    private JdbcTemplate template;

    private final String FIND_USER =
    """
        SELECT COUNT(username) FROM customers WHERE username = ? AND password = sha2(?, 224);        
    """;

    private final String INSERT_ORDER = 
    """
        INSERT INTO place_orders (order_id,payment_id,order_date,total,username) VALUES (?,?,?,?,?)        
    """;

    public boolean isUserValid(String username, String password){
        int userCount = Optional.ofNullable(template.queryForObject(FIND_USER,Integer.class,username, password)).orElse(0);
        return userCount > 0;
    }

    public boolean addOrderDetails(String username, Document toInsert){
        int added = template.update(INSERT_ORDER, toInsert.getString(ORDER_ID), toInsert.getString(PAYMENT_ID),
        toInsert.getString(ORDER_DATE),toInsert.getDouble(TOTAL),toInsert.getString(USERNAME));
        return added > 0;
    }


}
