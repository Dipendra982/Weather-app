


import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

public class CreateAccountForm extends JFrame {
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton signInButton;
    private JButton signUpButton;
    
    public CreateAccountForm() {
        setTitle("Create Account");
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
        ImageIcon imageIcon = new ImageIcon("/Users/dipendra/Desktop/Weatherapp/images (13).jpeg");
        Image image = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(image));
        imageLabel.setBounds(80, 0, 150, 150);
        mainPanel.add(imageLabel);
        
        // Create title
        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setBounds(70, 130, 200, 30);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(63, 57, 125));
        
        // Create labels
        JLabel nameLabel = createStyledLabel("Name", 40, 180);
        JLabel emailLabel = createStyledLabel("Email", 40, 240);
        JLabel passwordLabel = createStyledLabel("Password", 40, 300);
        
        // Create form fields
        nameField = createStyledTextField(40, 200);
        emailField = createStyledTextField(40, 260);
        passwordField = createStyledPasswordField(40, 320);
        
        // Create buttons
        signInButton = createStyledButton("Sign in", 40, 380);
        signUpButton = createStyledButton("Sign up", 160, 380);
        
        // Add components to panel
        mainPanel.add(titleLabel);
        mainPanel.add(nameLabel);
        mainPanel.add(emailLabel);
        mainPanel.add(passwordLabel);
        mainPanel.add(nameField);
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
        SwingUtilities.invokeLater(() -> new CreateAccountForm());
    }
}