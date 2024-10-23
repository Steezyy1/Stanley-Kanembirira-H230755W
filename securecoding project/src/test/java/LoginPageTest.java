import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import static org.junit.jupiter.api.Assertions.*;

public class LoginPageTest {
    @Test
    public void testPasswordHashing() {
        String password = "Str0ngPa$$word!";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        assertTrue(BCrypt.checkpw(password, hashedPassword));
    }
}
