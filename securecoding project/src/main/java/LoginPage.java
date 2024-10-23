import javax.swing.*;
import java.awt.*;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

public class LoginPage extends JFrame {
    private JFrame frame;
    private JTextField emailField;
    private JPasswordField passwordField;
    private int failedAttempts = 0;
    private static final int MAX_ATTEMPTS = 3;

    public LoginPage() {
        frame = new JFrame("Login Page");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create UI components
        frame.setLayout(new GridLayout(4, 2));

        frame.add(new JLabel("Email:"));
        emailField = new JTextField();
        frame.add(emailField);

        frame.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        frame.add(passwordField);

        // Show/Hide Password toggle
        JCheckBox showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.addActionListener(e -> {
            if (showPasswordCheckBox.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('*');
            }
        });
        frame.add(showPasswordCheckBox);

        JButton forgotPasswordButton = new JButton("Forgot Password?");
        frame.add(forgotPasswordButton);

        JButton backButton = new JButton("Back");
        frame.add(backButton);

        backButton.addActionListener(e -> {
            frame.dispose();
            new MainPage().setVisible(true);  // Go back to the main page
        });

        JButton loginButton = new JButton("Login");
        frame.add(loginButton);


        // Login button logic
        loginButton.addActionListener(e -> handleLogin());

        // Forgot password logic
        forgotPasswordButton.addActionListener(e -> handlePasswordRecovery());

        frame.setVisible(true);


    }



    private void handleLogin() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        try (Connection conn = MySQLConnector.getConnection()) {
            String query = "SELECT password FROM signup WHERE email_address = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHashedPassword = rs.getString("password");

                // Verify the password against the hashed password
                if (BCrypt.checkpw(password, storedHashedPassword)) {
                    JOptionPane.showMessageDialog(frame, "Login successful!");
                    frame.dispose();
                    new DisplayPage(email).setVisible(true);
                } else {
                    handleFailedLogin();
                }
            } else {
                handleFailedLogin();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "An error occurred during login.");
        }
    }

    private void handleFailedLogin() {
        failedAttempts++;
        if (failedAttempts >= MAX_ATTEMPTS) {
            JOptionPane.showMessageDialog(frame, "Account locked due to multiple failed login attempts.");
            frame.dispose();
            new MainPage().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid login. Attempts left: " + (MAX_ATTEMPTS - failedAttempts));
        }
    }

    private void handlePasswordRecovery() {
        String email = JOptionPane.showInputDialog(frame, "Enter your email to reset password:");
        if (email == null || email.trim().isEmpty()) {
            return;  // If cancel or empty input, do nothing
        }

        // Simulate password recovery process
        JOptionPane.showMessageDialog(frame, "A password reset link has been sent to " + email + ".");
        frame.dispose();
        new MainPage().setVisible(true);
    }
}