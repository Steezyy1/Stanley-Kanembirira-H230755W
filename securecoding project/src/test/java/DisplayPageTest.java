import org.junit.jupiter.api.Test;
import javax.swing.*;
import static org.junit.jupiter.api.Assertions.*;

public class DisplayPageTest {

    @Test
    public void testDisplayPageInitialization() {
        SwingUtilities.invokeLater(() -> {
            DisplayPage displayPage = new DisplayPage("user@example.com");

            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(displayPage);
            assertNotNull(frame, "Frame should not be null");

            // Check if frame is visible
            assertTrue(frame.isShowing(), "Frame should be visible");

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
