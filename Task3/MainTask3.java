package module13.Task3;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainTask3 {

    public static void main(String[] args) {
        int userId = 1; // Замініть на потрібний ідентифікатор користувача

        try {
            URL url = new URL("https://jsonplaceholder.typicode.com/users/" + userId + "/todos");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            StringBuilder response = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                response.append(output);
            }

            conn.disconnect();

            // Обробка JSON
            JSONArray todos = new JSONArray(response.toString());
            for (int i = 0; i < todos.length(); i++) {
                JSONObject todo = todos.getJSONObject(i);
                boolean completed = todo.getBoolean("completed");
                if (!completed) {
                    int id = todo.getInt("id");
                    String title = todo.getString("title");
                    System.out.println("User " + userId + " has an open task:");
                    System.out.println("ID: " + id);
                    System.out.println("Title: " + title);
                    System.out.println();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
