package WeatherReport;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class Asign2 {
    private static final String API_KEY = "b6907d289e10d714a6e88b30761fae22";
    private static final String API_URL = "https://samples.openweathermap.org/data/2.5/forecast/hourly?q=London,us&appid=" + API_KEY;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int option = -1;
        while (option != 0) {
            System.out.println("1. Get Temperature\n2. Get Wind Speed\n3. Get Pressure\n0. Exit");
            System.out.print("Enter an option: ");
            option = scanner.nextInt();
            scanner.nextLine();
            if (option == 0) {
                break;
            }
            System.out.print("Enter date and time (yyyy-MM-dd HH:mm:ss): ");
            String inputDateTime = scanner.nextLine();
            LocalDateTime dateTime = LocalDateTime.parse(inputDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            try {
                URL url = new URL(API_URL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                JSONObject json = new JSONObject(content.toString());
                JSONArray list = json.getJSONArray("list");
                for (int i = 0; i < list.length(); i++) {
                    JSONObject item = list.getJSONObject(i);
                    LocalDateTime itemDateTime = LocalDateTime.parse(item.getString("dt_txt"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    if (itemDateTime.equals(dateTime)) {
                        switch (option) {
                            case 1:
                                double temp = item.getJSONObject("main").getDouble("temp");
                                System.out.println("Temperature: " + temp + "K");
                                break;
                            case 2:
                                double windSpeed = item.getJSONObject("wind").getDouble("speed");
                                System.out.println("Wind Speed: " + windSpeed + "m/s");
                                break;
                            case 3:
                                double pressure = item.getJSONObject("main").getDouble("pressure");
                                System.out.println("Pressure: " + pressure + "hPa");
                                break;
                        }
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        scanner.close();
    }
}
