import org.junit.jupiter.api.Test;
import javax.swing.*;
import static org.junit.jupiter.api.Assertions.*;

public class MainPageTest {
    @Test
    public void testMainPageInitialization() {
        SwingUtilities.invokeLater(() -> {
            MainPage mainPage = new MainPage();
            mainPage.setVisible(true);

            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPage);
            assertNotNull(frame, "Frame should not be null");
            assertEquals("Main Page", frame.getTitle(), "Title should be 'Main Page'");

            // Cleanup
            frame.dispose();
        });

        // Pause for the UI thread to process
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
