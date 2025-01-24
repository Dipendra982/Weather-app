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

    public WeatherApp() {
        super("Weather App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Main panel with white background
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new BorderLayout());

        // Title label
        JLabel titleLabel = new JLabel("Weather App", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Input panel
        JPanel inputPanel = new JPanel(new FlowLayout());
        cityTextField = new JTextField(20);
        cityTextField.setFont(new Font("Arial", Font.PLAIN, 16));
        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("Arial", Font.BOLD, 14));
        searchButton.setBackground(new Color(30, 144, 255));
        searchButton.setForeground(Color.WHITE);
        searchButton.addActionListener(e -> fetchWeatherData());
        inputPanel.add(cityTextField);
        inputPanel.add(searchButton);
        mainPanel.add(inputPanel, BorderLayout.NORTH);

        // Weather panel
        weatherPanel = new JPanel();
        weatherPanel.setLayout(new BoxLayout(weatherPanel, BoxLayout.Y_AXIS));
        weatherPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.add(weatherPanel, BorderLayout.CENTER);

        // Weather labels
        weatherLabel = new JLabel("", SwingConstants.CENTER);
        weatherLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        weatherPanel.add(weatherLabel);

        loadingLabel = new JLabel("Loading...", SwingConstants.CENTER);
        loadingLabel.setVisible(false);
        weatherPanel.add(loadingLabel);

        errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        errorLabel.setVisible(false);
        weatherPanel.add(errorLabel);

        // 5-day forecast panel
        forecastPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        forecastPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
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
                        "<td align='center'><img src='thermometer.png' width='20' height='20'><br>Feels like<br>" + Math.round(feelsLike) + "°C</td>" +
                        "<td align='center'><img src='humidity.png' width='20' height='20'><br>Humidity<br>" + humidity + "%</td>" +
                        "<td align='center'><img src='wind.png' width='20' height='20'><br>Wind Speed<br>" + Math.round(windSpeed) + " km/h</td>" +
                        "</tr>" +
                        "<tr>" +
                        "<td align='center'><img src='pressure.png' width='20' height='20'><br>Pressure<br>" + pressure + " hPa</td>" +
                        "<td align='center'><img src='cloudiness.png' width='20' height='20'><br>Cloudiness<br>" + cloudiness + "%</td>" +
                        "<td align='center'><img src='sunrise.png' width='20' height='20'><br>Sunrise | Sunset<br>" + sunriseFormatted + " | " + sunsetFormatted + "</td>" +
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

            // Use try-catch to handle potential MalformedURLException
            try {
                JLabel iconLabel = new JLabel(new ImageIcon(new URL("http://openweathermap.org/img/wn/" + icon + "@2x.png")));
                JLabel tempLabel = new JLabel(Math.round(maxTemp) + "°C", SwingConstants.CENTER);
                tempLabel.setFont(new Font("Arial", Font.BOLD, 16));
                JLabel minMaxLabel = new JLabel("<html><font color='red'>" + Math.round(minTemp) + "°C</font> | <font color='blue'>" + Math.round(maxTemp) + "°C</font></html>", SwingConstants.CENTER);
                JLabel descLabel = new JLabel(description, SwingConstants.CENTER);

                dayPanel.add(dayLabel);
                dayPanel.add(iconLabel);
                dayPanel.add(tempLabel);
                dayPanel.add(minMaxLabel);
                dayPanel.add(descLabel);
            } catch (MalformedURLException e) {
                // Handle the exception, e.g., print an error message or show a default icon
                System.err.println("Error creating icon: " + e.getMessage());
            }
            forecastPanel.add(dayPanel);
        }
    }

    public static void main(String[] args) {
        new WeatherApp();
    }
}