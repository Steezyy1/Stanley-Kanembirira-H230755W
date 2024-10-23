import javax.swing.*;
import java.awt.*;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

public class DisplayPage extends JFrame {
    private JFrame frame;
    private JLabel nameLabel, surnameLabel, registrationNumberLabel;
    private JButton changePasswordButton;
    private boolean passwordChangedRecently = false; // Track password change state

    public DisplayPage(String email) {
        frame = new JFrame("Display Page");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create UI components
        nameLabel = new JLabel();
        surnameLabel = new JLabel();
        registrationNumberLabel = new JLabel();

        // Layout
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.add(nameLabel);
        frame.add(surnameLabel);
        frame.add(registrationNumberLabel);

        // Fetch user information
        fetchUserInfo(email);

        // Change Password button
        changePasswordButton = new JButton("Change Password");
        frame.add(changePasswordButton);

        // Change password button logic
        changePasswordButton.addActionListener(e -> handleChangePassword(email));

        // Logout button
        JButton logoutButton = new JButton("Logout");
        frame.add(logoutButton);

        logoutButton.addActionListener(e -> {
            frame.dispose();
            new MainPage().setVisible(true);
        });

        frame.setVisible(true);
    }

    void fetchUserInfo(String email) {
        try (Connection conn = MySQLConnector.getConnection()) {
            String query = "SELECT name, surname, registration_number FROM signup WHERE email_address = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nameLabel.setText("Name: " + rs.getString("name"));
                surnameLabel.setText("Surname: " + rs.getString("surname"));
                registrationNumberLabel.setText("Registration Number: " + rs.getString("registration_number"));
            } else {
                JOptionPane.showMessageDialog(frame, "Error fetching user information.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "An error occurred while fetching user information.");
        }
    }

    private void handleChangePassword(String email) {
        if (passwordChangedRecently) {
            JOptionPane.showMessageDialog(frame, "Password has just been changed. Please try again later.");
            return;
        }

        // Create a dialog for entering old and new passwords
        JDialog passwordDialog = new JDialog(frame, "Change Password", true);
        passwordDialog.setSize(300, 200);
        passwordDialog.setLayout(new GridLayout(4, 2));

        JTextField oldPasswordField = new JPasswordField();
        JTextField newPasswordField = new JPasswordField();

        passwordDialog.add(new JLabel("Old Password:"));
        passwordDialog.add(oldPasswordField);
        passwordDialog.add(new JLabel("New Password:"));
        passwordDialog.add(newPasswordField);

        JButton submitButton = new JButton("Submit");
        passwordDialog.add(submitButton);

        submitButton.addActionListener(e -> {
            String oldPassword = oldPasswordField.getText();
            String newPassword = newPasswordField.getText();

            if (PasswordStrengthChecker.check(newPassword) != 100) {
                JOptionPane.showMessageDialog(passwordDialog, "Weak password.");
                return;
            }

            if (updatePassword(email, oldPassword, newPassword)) {
                passwordChangedRecently = true; // Set the flag to prevent immediate changes
                JOptionPane.showMessageDialog(passwordDialog, "Password changed successfully!");
                passwordDialog.dispose();

                // Reset password change after 10 minutes
                Timer timer = new Timer(600000, evt -> passwordChangedRecently = false); // 10 minutes
                timer.setRepeats(false);
                timer.start();
            } else {
                JOptionPane.showMessageDialog(passwordDialog, "Invalid old password.");
            }
        });

        passwordDialog.setVisible(true);
    }


    private boolean updatePassword(String email, String oldPassword, String newPassword) {
        try (Connection conn = MySQLConnector.getConnection()) {
            // First, retrieve the stored hashed password for comparison
            String query = "SELECT password FROM signup WHERE email_address = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHashedPassword = rs.getString("password");

                // Verify if the old password entered by the user matches the stored hashed password
                if (BCrypt.checkpw(oldPassword, storedHashedPassword)) {

                    // If the old password is correct, hash the new password
                    String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(10));

                    // Update the password in the database
                    String updateQuery = "UPDATE signup SET password = ? WHERE email_address = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                    updateStmt.setString(1, hashedNewPassword);
                    updateStmt.setString(2, email);

                    int rowsUpdated = updateStmt.executeUpdate();
                    return rowsUpdated > 0; // Returns true if password was updated
                } else {
                    // Old password is incorrect
                    JOptionPane.showMessageDialog(frame, "Incorrect old password.");
                    return false;
                }
            } else {
                // User not found in the database
                JOptionPane.showMessageDialog(frame, "User not found.");
                return false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "An error occurred while changing password.");
            return false;
        }
    }

}