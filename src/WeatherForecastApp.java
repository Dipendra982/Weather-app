

import java.awt.*;
import javax.swing.*;

public class WeatherForecastApp {
    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("Weather Forecast");
        frame.setSize(400, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        // Create the main panel for background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw gradient background
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(98, 0, 238), 0, getHeight(), new Color(55, 0, 179));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(null);
        frame.add(mainPanel);

        // Weather Icon
        JLabel weatherIcon = new JLabel(new ImageIcon("/Users/dipendra/Desktop/Weatherapp/src/images (9) copy.jpeg")); // Replace with your icon path
        weatherIcon.setBounds(125, 100, 150, 150);
        mainPanel.add(weatherIcon);

        // App Title
        JLabel title = new JLabel("Weather", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        title.setBounds(100, 270, 200, 50);
        mainPanel.add(title);

        JLabel subtitle = new JLabel("Forecasts", SwingConstants.CENTER);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 28));
        subtitle.setForeground(new Color(255, 223, 0)); // Yellow text
        subtitle.setBounds(100, 320, 200, 40);
        mainPanel.add(subtitle);

        // // Get Start Button
        // JButton getStartButton = new JButton("Get Start");
        // getStartButton.setFont(new Font("Arial", Font.BOLD, 20));
        // getStartButton.setBackground(new Color(255, 223, 0)); // Yellow button
        // getStartButton.setForeground(Color.BLACK);
        // getStartButton.setFocusPainted(false);
        // getStartButton.setBorderPainted(false);
        // getStartButton.setBounds(120, 400, 160, 50);
        // getStartButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Starting Weather Forecast..."));   //second frame stop
        // mainPanel.add(getStartButton);
         // Get Start Button
         JButton startButton = new JButton("Get Start");
         startButton.setFont(new Font("SansSerif", Font.BOLD, 18));
         startButton.setBounds(125, 450, 150, 50);
         startButton.setBackground(new Color(255, 204, 0));
         startButton.setForeground(Color.BLACK);
         startButton.setFocusPainted(false);
         mainPanel.add(startButton);


        // Make the frame visible
        frame.setVisible(true);
    }
}
