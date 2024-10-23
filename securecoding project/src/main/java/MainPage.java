import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainPage extends JFrame {
    private JFrame frame;

    public MainPage() {
        frame = new JFrame("Main Page");
        frame.setSize(450, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create UI components
        JLabel label = new JLabel("Welcome To Your HIT Student Page!");
        JButton signUpButton = new JButton("Sign Up");
        JLabel label2 = new JLabel("Already Have An Account?");
        JButton loginButton = new JButton("Login");

        // Layout
        frame.setLayout(new GridLayout(3, 1));
        frame.add(label);
        frame.add(signUpButton);
        frame.add(label2);
        frame.add(loginButton);

        // Sign-up button functionality
        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();  // Close the main page frame
                SignUpPage signUpPage = new SignUpPage(MainPage.this);
                signUpPage.setVisible(true);  // Show the SignUpPage
            }
        });

        // Login button functionality
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();  // Close main page
                LoginPage loginPage = new LoginPage();
                loginPage.setVisible(true);  // Show the LoginPage
            }
        });
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);  // Set the frame visibility
    }

    public void showFrame() {
        frame.setVisible(true);  // Make the frame visible
    }

    public static void main(String[] args) {
        MainPage mainPage = new MainPage();
        mainPage.showFrame();  // Display the main page when the program starts
    }
}