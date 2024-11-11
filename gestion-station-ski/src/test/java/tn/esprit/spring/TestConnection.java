package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import javax.sql.DataSource;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class TestConnection {

    @Autowired
    private DataSource dataSource;

    @Test
    public void testConnection() throws Exception {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        System.out.println("Database connection successful!");
        connection.close();
    }
}