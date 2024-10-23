import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordStrengthCheckerTest {
    @Test
    public void testPasswordStrength() {
        assertEquals(100, PasswordStrengthChecker.check("Str0ngPa$$word!"));
        assertNotEquals(100, PasswordStrengthChecker.check("weak"));
    }
}
