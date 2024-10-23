import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import org.mindrot.jbcrypt.BCrypt;


public class SignUpPage extends JFrame {
    private JFrame frame;
    private JTextField nameField, surnameField, ageField, sexField, phoneNumberField, partField, departmentCodeField, registrationField, emailField;
    private JPasswordField passwordField, confirmPasswordField;
    private JProgressBar passwordStrengthBar;
    private JCheckBox termsCheckBox;
    private JTextField captchaField;
    private String captcha;

    public SignUpPage(MainPage mainPage) {
        frame = new JFrame("Sign Up Page");
        frame.setSize(500, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // UI components for the fields
        frame.setLayout(new GridLayout(16, 2));

        frame.add(new JLabel("Name(s):"));
        nameField = new JTextField();
        frame.add(nameField);

        frame.add(new JLabel("Surname:"));
        surnameField = new JTextField();
        frame.add(surnameField);

        frame.add(new JLabel("Age:"));
        ageField = new JTextField();
        frame.add(ageField);

        frame.add(new JLabel("Sex(M/F):"));
        sexField = new JTextField();
        frame.add(sexField);

        frame.add(new JLabel("Phone Number: (+263)"));
        phoneNumberField = new JTextField();
        frame.add(phoneNumberField);

        frame.add(new JLabel("Part:"));
        partField = new JTextField();
        frame.add(partField);

        frame.add(new JLabel("Department Code:"));
        departmentCodeField = new JTextField();
        frame.add(departmentCodeField);

        frame.add(new JLabel("Registration Number:"));
        registrationField = new JTextField();
        frame.add(registrationField);

        frame.add(new JLabel("HIT mail Address:"));
        emailField = new JTextField();
        frame.add(emailField);

        frame.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        frame.add(passwordField);

        frame.add(new JLabel("Password Strength:"));
        // Password strength indicator
        passwordStrengthBar = new JProgressBar(0, 100);
        frame.add(passwordStrengthBar);
        passwordField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updatePasswordStrength(); }
            public void removeUpdate(DocumentEvent e) { updatePasswordStrength(); }
            public void changedUpdate(DocumentEvent e) { updatePasswordStrength(); }

            private void updatePasswordStrength() {
                String password = new String(passwordField.getPassword());
                int strength = PasswordStrengthChecker.check(password);  // Custom strength checker
                passwordStrengthBar.setValue(strength);
            }
        });


        frame.add(new JLabel("Confirm Password:"));
        confirmPasswordField = new JPasswordField();
        frame.add(confirmPasswordField);

        // CAPTCHA
        captcha = generateCaptcha();
        frame.add(new JLabel("Enter CAPTCHA: " + captcha));
        captchaField = new JTextField();
        frame.add(captchaField);


        JCheckBox showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.addActionListener(e -> {
            if (showPasswordCheckBox.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('*');
            }
        });
        frame.add(showPasswordCheckBox);


        // Terms & Conditions
        String linkText = "<html>I agree to the <a href=''>Terms & Conditions</a></html>";
        termsCheckBox = new JCheckBox(linkText);
        termsCheckBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        termsCheckBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    Rectangle linkRect = termsCheckBox.getBounds();
                    String text = termsCheckBox.getText();
                    FontMetrics fm = termsCheckBox.getFontMetrics(termsCheckBox.getFont());
                    int startLink = text.indexOf("<a");
                    int endLink = text.indexOf("</a>");
                    int startTextOffset = fm.stringWidth(text.substring(0, startLink));
                    int endTextOffset = fm.stringWidth(text.substring(0, endLink));
                    if (e.getX() >= startTextOffset && e.getX() <= endTextOffset) {
                        openLink("https://www.hit.ac.zw/downloads/about/Harare-Institute-of-Technology-general-regulations.pdf");
                    }
                }
            }
        });
        frame.add(termsCheckBox);


        JButton backButton = new JButton("Back");
        frame.add(backButton);

        JButton signUpButton = new JButton("Sign Up");
        frame.add(signUpButton);


        signUpButton.addActionListener(e -> handleSignUp());
        backButton.addActionListener(e -> {
            frame.dispose();
            mainPage.setVisible(true);
        });

        frame.setVisible(true);
    }



    private void handleSignUp() {
        try {
            // Get the field values
            String name = nameField.getText();
            String surname = surnameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String sex = sexField.getText();
            String phoneNumber = phoneNumberField.getText();
            String part = partField.getText();
            String departmentCode = departmentCodeField.getText();
            String registrationNumber = registrationField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            String captchaInput = captchaField.getText();


            // Validate CAPTCHA
            if (!captcha.equals(captchaInput)) {
                JOptionPane.showMessageDialog(frame, "Invalid CAPTCHA.");
                return;
            }

            // Additional validation and password match validation
            if (!validateFields(name, surname, age, sex, phoneNumber, part, departmentCode, registrationNumber, email)) {
                JOptionPane.showMessageDialog(frame, "Please fill out all fields correctly.");
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(frame, "Passwords do not match!");
                return;
            }

            if (PasswordStrengthChecker.check(password) != 100) {
                JOptionPane.showMessageDialog(frame, "Weak password");
                return;
            }

            if (!termsCheckBox.isSelected()) {
                JOptionPane.showMessageDialog(frame, "You must agree to the Terms & Conditions.");
                return;
            }

            try (Connection conn = MySQLConnector.getConnection()) {
                // Check if the email already exists in the database
                String checkEmailQuery = "SELECT email_address FROM signup WHERE email_address = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkEmailQuery);
                checkStmt.setString(1, email.trim().toLowerCase());
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(frame, "Email already exists.");
                    return;  // Exit the method if email already exists
                }

                // Hash the password before storing it
                String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));


                // Insert into the database
                String query = "INSERT INTO signup (name, surname, age, sex, phone_number, " +
                        "part, department_code, registration_number, email_address, password) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, name.trim());
                stmt.setString(2, surname.trim());
                stmt.setInt(3, age);
                stmt.setString(4, sex.trim());
                stmt.setString(5, phoneNumber);
                stmt.setString(6, part.trim());
                stmt.setString(7, departmentCode.trim());
                stmt.setString(8, registrationNumber.trim());
                stmt.setString(9, email.trim().toLowerCase());  // Ensure email is lowercase
                stmt.setString(10, hashedPassword);

                stmt.executeUpdate();
                JOptionPane.showMessageDialog(frame, "Sign-up successful!");

                frame.dispose();
                new LoginPage().setVisible(true);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error saving user details: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid input in numeric fields.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "An unexpected error occurred.");
        }
    }


    String generateCaptcha() {
        Random random = new Random();
        int length = 6;
        String captcha = "";
        for (int i = 0; i < length; i++) {
            captcha += (char) (random.nextInt(26) + 'A');
        }
        return captcha;
    }


    private boolean validateFields(String name, String surname, int age, String sex,
                                   String phoneNumber, String part, String departmentCode,
                                   String registrationNumber, String email) {
        // Basic validation rules (adjust as needed)
        if (name == null || name.isEmpty() || name.length() > 30 || surname == null || surname.length() > 30 || surname.isEmpty()) return false;
        if (age < 18 || age > 60) return false;  // Age range check
        if (!(sex.equalsIgnoreCase("M") || sex.equalsIgnoreCase("F"))) return false;
        if (phoneNumber.length() < 9 || phoneNumber.length() > 10) return false;  // Phone number check
        if (part == null || part.isEmpty() || part.length() > 3 || departmentCode == null || departmentCode.length() > 6 || departmentCode.isEmpty()) return false;
        if (registrationNumber == null || registrationNumber.length() > 8 || !(registrationNumber.toLowerCase().startsWith("h"))) return false;
        if (email == null || email.length() > 18 || !(email.equals(registrationNumber.toLowerCase() + "@hit.ac.zw"))) return false;

        return true;
    }

    private void openLink(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Could not open the link. Please visit the website manually.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}