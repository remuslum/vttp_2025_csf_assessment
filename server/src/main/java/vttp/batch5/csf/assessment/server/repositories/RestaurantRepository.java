package vttp.batch5.csf.assessment.server.repositories;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

// Use the following class for MySQL database
@Repository
public class RestaurantRepository {

    @Autowired
    private JdbcTemplate template;

    private final String FIND_USER =
    """
        SELECT COUNT(username) FROM customers WHERE username = ?;        
    """;

    public boolean isUserValid(String username){
        int userCount = Optional.ofNullable(template.queryForObject(FIND_USER,Integer.class,username)).orElse(0);
        return userCount > 0;
    }

    
}
