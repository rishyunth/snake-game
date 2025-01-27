import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegistrationFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton registerButton;

    RegistrationFrame() {
        this.setTitle("Registration");
        this.setSize(600, 400); // Increased width
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close this frame on registration
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        // Create a JPanel with a background color
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(230, 230, 250)); // Light Lavender background color

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 20, 10, 20); // Increased padding

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameField = new JTextField(25); // Increased width
        usernameField.setPreferredSize(new Dimension(200, 30)); // Increased height

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField = new JPasswordField(25); // Increased width
        passwordField.setPreferredSize(new Dimension(200, 30)); // Increased height

        JButton submitButton = new JButton("Submit");
        submitButton.setBackground(new Color(70, 130, 180)); // Steel Blue
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setFont(new Font("Arial", Font.PLAIN, 16));

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(submitButton, gbc);

        this.add(panel);

       submitButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               String username = usernameField.getText();
               char[] passwordChars = passwordField.getPassword();

               if (username.isEmpty() || passwordChars.length == 0) {
                   JOptionPane.showMessageDialog(RegistrationFrame.this, "Please enter both username and password.", "Registration Error", JOptionPane.ERROR_MESSAGE);
                   return;
               }
               String password = new String(passwordChars);

               boolean registrationResult = registerUser(username, password);

               if (registrationResult) {
                   JOptionPane.showMessageDialog(RegistrationFrame.this, "Registration successful.", "Registration Success", JOptionPane.INFORMATION_MESSAGE);
                   RegistrationFrame.this.dispose(); // Close the registration frame after successful registration
               } else {
                   JOptionPane.showMessageDialog(RegistrationFrame.this, "Registration failed. User already exists.", "Registration Error", JOptionPane.ERROR_MESSAGE);
               }
           }
       });

        this.setVisible(true);
    }

    private boolean registerUser(String name, String pass){


        try {
        	Connection connection=DriverManager.getConnection("jdbc:mysql://localhost/snakegame", "root", null);
       

            String insertQuery = "INSERT INTO Login_info (username, password) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, pass);

            int rowsAffected = preparedStatement.executeUpdate();
            connection.close();

             return rowsAffected > 0;

        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Failed to connect to database");
            return false;
        }
    }
}



