import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

public class SignIn extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton signInButton;
    private JButton signUpButton;

    public SignIn() {
        setTitle("Sign In");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set purple background
        getContentPane().setBackground(new Color(147, 112, 219));
        setLayout(null);

        // Create white rounded panel
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
            }
        };
        mainPanel.setLayout(null);
        mainPanel.setBounds(50, 50, 300, 480);
        mainPanel.setOpaque(false);

        // Add image
        ImageIcon imageIcon = new ImageIcon("/Users/dipendra/Desktop/Weatherapp/src/images (2) copy.jpeg");
        Image image = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(image));
        imageLabel.setBounds(100, 20, 100, 100);
        mainPanel.add(imageLabel);

        // Create title
        JLabel titleLabel = new JLabel("Sign In", SwingConstants.CENTER);
        titleLabel.setBounds(0, 130, 300, 30);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(63, 57, 125));
        mainPanel.add(titleLabel);

        // Create labels
        JLabel emailLabel = createStyledLabel("Email", 40, 180);
        JLabel passwordLabel = createStyledLabel("Password", 40, 240);

        // Create form fields
        emailField = createStyledTextField(40, 200);
        passwordField = createStyledPasswordField(40, 260);

        // Create buttons
        signInButton = createStyledButton("Sign in", 40, 350);
        signUpButton = createStyledButton("Sign up", 160, 350);

        // Add sign in button action listener
        signInButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (email.equals("demo@gmail.com") && password.equals("Test")) {
                dispose();
                SwingUtilities.invokeLater(() -> new WeatherApp());
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add sign up button action listener
        signUpButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                new CreateAccountForm();
                dispose(); // Close current window
            });
        });

        // Add components to panel
        mainPanel.add(emailLabel);
        mainPanel.add(passwordLabel);
        mainPanel.add(emailField);
        mainPanel.add(passwordField);
        mainPanel.add(signInButton);
        mainPanel.add(signUpButton);

        // Add panel to frame
        add(mainPanel);

        setVisible(true);
    }

    private JLabel createStyledLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 220, 20);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(new Color(63, 57, 125));
        return label;
    }

    private JTextField createStyledTextField(int x, int y) {
        JTextField field = new JTextField();
        field.setBounds(x, y, 220, 30);
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        return field;
    }

    private JPasswordField createStyledPasswordField(int x, int y) {
        JPasswordField field = new JPasswordField();
        field.setBounds(x, y, 220, 30);
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        return field;
    }

    private JButton createStyledButton(String text, int x, int y) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
                g2.setColor(getForeground());
                FontMetrics fm = g2.getFontMetrics();
                Rectangle2D r = fm.getStringBounds(getText(), g2);
                int x = (getWidth() - (int) r.getWidth()) / 2;
                int y = (getHeight() - (int) r.getHeight()) / 2 + fm.getAscent();
                g2.drawString(getText(), x, y);
            }
        };
        button.setBounds(x, y, 100, 35);
        button.setBackground(new Color(63, 57, 125));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SignIn());
    }
}