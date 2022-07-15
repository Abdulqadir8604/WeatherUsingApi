import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        String api_key = "179c6873c8c546b895c152355221407";
        //get public ip address
        String ip_address = getPublicIp();

        //get weather data
        getWeatherData(ip_address, api_key);

    }

    private static void getWeatherData(String ip_address, String api_key) throws IOException, InterruptedException {
        String url = "https://api.weatherapi.com/v1/current.json?key="+api_key+"&q="+ip_address+"&aqi=no";
        //create http client
        HttpClient client = HttpClient.newHttpClient();
        //create http request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //System.out.println(response.body());

        //parse json data
        JSONObject jsonObject = new JSONObject(response.body());

        //current location
        JSONObject location = jsonObject.getJSONObject("location");
        System.out.println("Current location: "+location.getString("name"));

        //local time
        JSONObject current_time = jsonObject.getJSONObject("location");
        System.out.println("Local time: "+current_time.getString("localtime"));

        //current weather data
        JSONObject current = jsonObject.getJSONObject("current");
        System.out.println("Current temperature: " + current.getDouble("temp_c") + "°C");
        System.out.println("Feels like: " + current.getDouble("feelslike_c") + "°C");
        System.out.println("Current weather: " + current.getJSONObject("condition").getString("text"));
        System.out.println("Current humidity: " + current.getDouble("humidity") + "%");
        System.out.println("Current wind speed: " + current.getDouble("wind_kph") + "km/h");
        System.out.println("Current wind direction: " + current.getString("wind_dir"));
    }

    private static String getPublicIp() {
        String ip = "";
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api64.ipify.org?format=json"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject json = new JSONObject(response.body());
            ip = json.getString("ip");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Public IP: " + ip);
        return ip;
    }

}
