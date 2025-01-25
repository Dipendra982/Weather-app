import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherApp extends JFrame {

    private JTextField cityTextField;
    private JLabel weatherLabel;
    private JLabel loadingLabel;
    private JLabel errorLabel;
    private JPanel weatherPanel;
    private JPanel forecastPanel;
    private ImageIcon cloudnessIcon;
    private ImageIcon feelsLikeIcon;
    private ImageIcon humidityIcon;
    private ImageIcon pressureIcon;
    private ImageIcon sunriseSunsetIcon;
    private ImageIcon windSpeedIcon;

    public WeatherApp() {
        super("Weather App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Load weather icons
        cloudnessIcon = new ImageIcon("/Users/sanju/Downloads/Weather-app-main/src/Cloudness.jpeg");
        feelsLikeIcon = new ImageIcon("/Users/sanju/Downloads/Weather-app-main/src/FeelsLike.jpeg");
        humidityIcon = new ImageIcon("/Users/sanju/Downloads/Weather-app-main/src/Humidity.jpeg");
        pressureIcon = new ImageIcon("/Users/sanju/Downloads/Weather-app-main/src/Pressure.png");
        sunriseSunsetIcon = new ImageIcon("/Users/sanju/Downloads/Weather-app-main/src/SuniriseSunset.png");
        windSpeedIcon = new ImageIcon("/Users/sanju/Downloads/Weather-app-main/src/WindSpeed.jpeg");

        // Resize icons to appropriate size
        int iconSize = 30;
        cloudnessIcon = new ImageIcon(cloudnessIcon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH));
        feelsLikeIcon = new ImageIcon(feelsLikeIcon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH));
        humidityIcon = new ImageIcon(humidityIcon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH));
        pressureIcon = new ImageIcon(pressureIcon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH));
        sunriseSunsetIcon = new ImageIcon(sunriseSunsetIcon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH));
        windSpeedIcon = new ImageIcon(windSpeedIcon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH));

        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient =  new GradientPaint(0, 0, new Color(111, 78, 198), 0, getHeight(), new Color(111, 56, 204));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());

        // Title label with enhanced styling
        JLabel titleLabel = new JLabel("Weather Forecast", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Enhanced input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setOpaque(false);
        inputPanel.setBorder(new EmptyBorder(0, 20, 20, 20));
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));

        // Styled search textfield
        cityTextField = new JTextField(20);
        cityTextField.setFont(new Font("Arial", Font.PLAIN, 18));
        cityTextField.setPreferredSize(new Dimension(300, 40));
        cityTextField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 2, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        cityTextField.setBackground(new Color(255, 255, 255, 50));
        cityTextField.setForeground(Color.WHITE);
        cityTextField.setCaretColor(Color.WHITE);

        // Styled search button
        JButton searchButton = new JButton("Search Weather");
        searchButton.setFont(new Font("Arial", Font.BOLD, 16));
        searchButton.setPreferredSize(new Dimension(150, 40));
        searchButton.setBackground(new Color(255, 204, 0));
        searchButton.setForeground(Color.BLACK);
        searchButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        searchButton.setFocusPainted(false);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Button hover effect
        searchButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                searchButton.setBackground(new Color(255, 215, 50));
            }
            public void mouseExited(MouseEvent e) {
                searchButton.setBackground(new Color(255, 204, 0));
            }
        });
        
        searchButton.addActionListener(e -> fetchWeatherData());
        
        inputPanel.add(cityTextField);
        inputPanel.add(searchButton);
        mainPanel.add(inputPanel, BorderLayout.NORTH);

        // Weather panel
        weatherPanel = new JPanel();
        weatherPanel.setLayout(new BoxLayout(weatherPanel, BoxLayout.Y_AXIS));
        weatherPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        weatherPanel.setOpaque(false);
        mainPanel.add(weatherPanel, BorderLayout.CENTER);

        // Weather labels
        weatherLabel = new JLabel("", SwingConstants.CENTER);
        weatherLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        weatherLabel.setForeground(Color.WHITE);
        weatherPanel.add(weatherLabel);

        loadingLabel = new JLabel("Loading...", SwingConstants.CENTER);
        loadingLabel.setForeground(Color.WHITE);
        loadingLabel.setVisible(false);
        weatherPanel.add(loadingLabel);

        errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setForeground(new Color(255, 100, 100));
        errorLabel.setVisible(false);
        weatherPanel.add(errorLabel);

        // 5-day forecast panel
        forecastPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        forecastPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        forecastPanel.setOpaque(false);
        mainPanel.add(forecastPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void fetchWeatherData() {
        String city = cityTextField.getText().trim();
        if (city.isEmpty()) {
            return;
        }

        loadingLabel.setVisible(true);
        errorLabel.setVisible(false);
        weatherLabel.setText("");
        forecastPanel.removeAll(); // Clear previous forecast

        Thread thread = new Thread(() -> {
            try {
                String apiKey = "98589b7ad9639c70333cb7ef4c19511b"; // Replace with your actual API key
                String currentWeatherUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&appid=" + apiKey;
                String forecastUrl = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&units=metric&appid=" + apiKey;

                // Fetch current weather data
                JSONObject currentWeatherJson = fetchJsonData(currentWeatherUrl);
                if (currentWeatherJson == null) {
                    throw new IOException("Failed to fetch current weather data.");
                }

                // Fetch 5-day forecast data
                JSONObject forecastJson = fetchJsonData(forecastUrl);
                if (forecastJson == null) {
                    throw new IOException("Failed to fetch forecast data.");
                }

                // Update UI with current weather data
                SwingUtilities.invokeLater(() -> updateWeatherUI(currentWeatherJson));

                // Update UI with 5-day forecast data
                SwingUtilities.invokeLater(() -> updateForecastUI(forecastJson));

            } catch (IOException ex) {
                SwingUtilities.invokeLater(() -> {
                    errorLabel.setText("Failed to fetch weather data. Please try again.");
                    errorLabel.setVisible(true);
                });
            } finally {
                SwingUtilities.invokeLater(() -> loadingLabel.setVisible(false));
            }
        });
        thread.start();
    }

    private JSONObject fetchJsonData(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            return new JSONObject(response.toString());
        } else {
            return null;
        }
    }

    private void updateWeatherUI(JSONObject json) {
        String name = json.getString("name");
        String country = json.getJSONObject("sys").getString("country");
        String description = json.getJSONArray("weather").getJSONObject(0).getString("description");
        double temp = json.getJSONObject("main").getDouble("temp");
        double feelsLike = json.getJSONObject("main").getDouble("feels_like");
        int humidity = json.getJSONObject("main").getInt("humidity");
        double windSpeed = json.getJSONObject("wind").getDouble("speed") * 3.6;
        int pressure = json.getJSONObject("main").getInt("pressure");
        int cloudiness = json.getJSONObject("clouds").getInt("all");
        long sunrise = json.getJSONObject("sys").getLong("sunrise") * 1000;
        long sunset = json.getJSONObject("sys").getLong("sunset") * 1000;

        // Format sunrise and sunset time
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm:ss a")
                .withZone(ZoneId.systemDefault());
        String sunriseFormatted = timeFormatter.format(Instant.ofEpochMilli(sunrise));
        String sunsetFormatted = timeFormatter.format(Instant.ofEpochMilli(sunset));

        weatherLabel.setText(
                "<html><center>" + description + "<br>" +
                        "<br>" +
                        "<table width='100%'>" +
                        "<tr>" +
                        "<td align='center'><img src='" + feelsLikeIcon.toString() + "'><br>Feels like<br>" + Math.round(feelsLike) + "°C</td>" +
                        "<td align='center'><img src='" + humidityIcon.toString() + "'><br>Humidity<br>" + humidity + "%</td>" +
                        "<td align='center'><img src='" + windSpeedIcon.toString() + "'><br>Wind Speed<br>" + Math.round(windSpeed) + " km/h</td>" +
                        "</tr>" +
                        "<tr>" +
                        "<td align='center'><img src='" + pressureIcon.toString() + "'><br>Pressure<br>" + pressure + " hPa</td>" +
                        "<td align='center'><img src='" + cloudnessIcon.toString() + "'><br>Cloudiness<br>" + cloudiness + "%</td>" +
                        "<td align='center'><img src='" + sunriseSunsetIcon.toString() + "'><br>Sunrise | Sunset<br>" + sunriseFormatted + " | " + sunsetFormatted + "</td>" +
                        "</tr>" +
                        "</table>" +
                        "<br>" +
                        "5-Day Forecast" +
                        "</center></html>"
        );
    }

    private void updateForecastUI(JSONObject json) {
        forecastPanel.removeAll(); // Clear previous forecast
        JSONArray list = json.getJSONArray("list");
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_YEAR);

        // Initialize an index to track the forecast data
        int index = 0;
        // Loop through the next 5 days
        for (int i = 0; i < 5; i++) {
            // Calculate the target day of the year
            int targetDay = currentDay + i;
            if (targetDay > calendar.getActualMaximum(Calendar.DAY_OF_YEAR)) {
                targetDay -= calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
            }

            double minTemp = Double.MAX_VALUE;
            double maxTemp = Double.MIN_VALUE;
            String icon = null;
            String description = null;

            // Find the data for the target day
            while (index < list.length()) {
                JSONObject dayData = list.getJSONObject(index);
                long timestamp = dayData.getLong("dt") * 1000;
                calendar.setTimeInMillis(timestamp);
                int forecastDay = calendar.get(Calendar.DAY_OF_YEAR);

                // If the forecast day matches the target day, extract data
                if (forecastDay == targetDay) {
                    double temp = dayData.getJSONObject("main").getDouble("temp");
                    if (temp < minTemp) {
                        minTemp = temp;
                    }
                    if (temp > maxTemp) {
                        maxTemp = temp;
                    }
                    icon = dayData.getJSONArray("weather").getJSONObject(0).getString("icon");
                    description = dayData.getJSONArray("weather").getJSONObject(0).getString("description");
                    index++; // Move to the next data point
                    break; // Exit the loop after finding the data for the target day
                } else {
                    index++; // Move to the next data point if the current one doesn't match
                }
            }

            // Create panel for each day
            JPanel dayPanel = new JPanel();
            dayPanel.setLayout(new BoxLayout(dayPanel, BoxLayout.Y_AXIS));
            dayPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

            // Get the day of the week for display
            calendar.set(Calendar.DAY_OF_YEAR, targetDay);
            String dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, java.util.Locale.US);
            JLabel dayLabel = new JLabel(dayOfWeek, SwingConstants.CENTER);

            JLabel tempLabel = new JLabel(Math.round(maxTemp) + "°C", SwingConstants.CENTER);
            tempLabel.setFont(new Font("Arial", Font.BOLD, 16));
            JLabel minMaxLabel = new JLabel("<html><font color='red'>" + Math.round(minTemp) + "°C</font> | <font color='blue'>" + Math.round(maxTemp) + "°C</font></html>", SwingConstants.CENTER);
            JLabel descLabel = new JLabel(description, SwingConstants.CENTER);

            dayPanel.add(dayLabel);
            dayPanel.add(tempLabel);
            dayPanel.add(minMaxLabel);
            dayPanel.add(descLabel);
            
            forecastPanel.add(dayPanel);
        }
    }

    public static void main(String[] args) {
        new WeatherApp();
    }
}