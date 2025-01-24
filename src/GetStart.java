import java.awt.*;
import javax.swing.*;

public class GetStart {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Weather Forecast");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 700);
            frame.setResizable(false);

            WeatherPanel panel = new WeatherPanel();
            frame.add(panel);

            frame.setVisible(true);
        });
    }

    static class WeatherPanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            // Gradient Background
            GradientPaint gradient = new GradientPaint(0, 0, new Color(111, 78, 198), 0, getHeight(), new Color(111, 56, 204));
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }

        public WeatherPanel() {
            setLayout(null);

            // Weather Icon (Placeholder)
            JLabel weatherIcon = new JLabel();
            weatherIcon.setIcon(new ImageIcon("/Users/dipendra/Desktop/Weatherapp/src/images (4) copy.jpeg")); // Replace with your weather icon image
            weatherIcon.setBounds(120, 100, 150, 150);
            add(weatherIcon);

            // Weather Text
            JLabel titleLabel = new JLabel("Weather", SwingConstants.CENTER);
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setBounds(100, 300, 200, 50);
            add(titleLabel);

            JLabel subLabel = new JLabel("ForeCasts", SwingConstants.CENTER);
            subLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
            subLabel.setForeground(new Color(255, 204, 0));
            subLabel.setBounds(100, 350, 200, 50);
            add(subLabel);

            // Get Start Button
            JButton startButton = new JButton("Get Start");
            startButton.setFont(new Font("SansSerif", Font.BOLD, 18));
            startButton.setBounds(125, 450, 150, 50);
            startButton.setBackground(new Color(255, 204, 0));
            startButton.setForeground(Color.BLACK);
            startButton.setFocusPainted(false);
            add(startButton);
        }
    }
}
