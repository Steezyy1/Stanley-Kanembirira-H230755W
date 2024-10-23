import org.junit.jupiter.api.Test;
import javax.swing.*;
import static org.junit.jupiter.api.Assertions.*;

public class SignUpPageTest {
    @Test
    public void testCaptchaGeneration() {
        SignUpPage signUpPage = new SignUpPage(new MainPage());
        String captcha = signUpPage.generateCaptcha();
        assertNotNull(captcha);
        assertEquals(6, captcha.length());
    }
}
