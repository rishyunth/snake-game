import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    LoginFrame() {
        this.setTitle("Login");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 400);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(230, 230, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 20, 10, 20);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameField = new JTextField(25); 
        usernameField.setPreferredSize(new Dimension(200, 30)); 

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField = new JPasswordField(25); // Increased width
        passwordField.setPreferredSize(new Dimension(200, 30)); // Increased height

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        JButton startButton = new JButton("Start trial");

        // Style the buttons
        loginButton.setBackground(new Color(70, 130, 180)); // Steel Blue
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Arial", Font.PLAIN, 16));
        registerButton.setBackground(new Color(60, 179, 113)); // Medium Sea Green
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setFont(new Font("Arial", Font.PLAIN, 16));
        startButton.setBackground(new Color(239, 145, 109)); // Red-Orange
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setFont(new Font("Arial", Font.PLAIN, 16));

        // Add components to the panel
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel()); // Empty label for spacing
        panel.add(loginButton);
        panel.add(registerButton);
        panel.add(startButton);

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
        panel.add(loginButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(registerButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(startButton, gbc);
        // Add the panel to the frame
        this.add(panel);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new GameFrame("New User");
            }
        });
        // Register an ActionListener for the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] passwordChars = passwordField.getPassword();
                // You can handle login logic here
                String password = new String(passwordChars);

                // Perform a basic login check (replace this with your actual authentication logic)
                if (isValidUser(username, password)) {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Login successful.", "Login Success", JOptionPane.INFORMATION_MESSAGE);
                    // Close the login frame and open the game frame
                    dispose();
                    new GameFrame(username);

                } else if (username.isEmpty() && password.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Please enter Username and Password", "Login Error", JOptionPane.ERROR_MESSAGE);

                } else if (username.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Please enter Username ", "Login Error", JOptionPane.ERROR_MESSAGE);

                } else if (password.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Please enter Password", "Login Error", JOptionPane.ERROR_MESSAGE);

                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Invalid username or password.", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegistrationFrame();
            }
        });
        this.setVisible(true);
    }



    private boolean isValidUser(String username, String password) {

        try (Connection connection=DriverManager.getConnection("jdbc:mysql://localhost/snakegame", "root", null)) {
            String query = "SELECT * FROM Login_info WHERE username = ? AND password = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2,password);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // User exists in the database, now check if the password matches
                        String storedUsername = resultSet.getString("username");
                        String storedPassword = resultSet.getString("password"); // Replace "password" with the actual column name in your database
                        return password.equals(storedPassword);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}