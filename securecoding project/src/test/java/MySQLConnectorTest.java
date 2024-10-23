import org.junit.jupiter.api.Test;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;

public class MySQLConnectorTest {
    @Test
    public void testConnection() {
        try {
            Connection conn = MySQLConnector.getConnection();
            assertNotNull(conn);
        } catch (Exception e) {
            fail("Connection should not throw an exception");
        }
    }
}
