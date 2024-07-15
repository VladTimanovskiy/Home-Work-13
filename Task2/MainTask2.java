package module13.Task2;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class MainTask2 {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    private static final HttpClient httpClient = HttpClients.createDefault();

    public static void main(String[] args) throws IOException {
        int userId = 1;  // ID користувача
        int postId = getLastPostId(userId);  // Отримуємо ID останнього поста користувача

        if (postId != -1) {
            JSONArray comments = getCommentsForPost(postId);  // Отримуємо коментарі до останнього поста
            saveCommentsToFile(userId, postId, comments);  // Зберігаємо коментарі у файл
            System.out.println("Коментарі були успішно збережені у файл.");
        } else {
            System.out.println("Не вдалося отримати ID останнього поста користувача.");
        }
    }

    private static int getLastPostId(int userId) throws IOException {
        String url = BASE_URL + "/users/" + userId + "/posts";
        HttpGet request = new HttpGet(url);
        HttpResponse response = httpClient.execute(request);
        int lastPostId = -1;

        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String responseBody = EntityUtils.toString(entity);
                JSONArray posts = new JSONArray(responseBody);

                if (posts.length() > 0) {
                    // Останній пост буде той, у якого найбільший id
                    JSONObject lastPost = posts.getJSONObject(posts.length() - 1);
                    lastPostId = lastPost.getInt("id");
                }
            }
        } finally {
            response.getEntity().getContent().close();
        }

        return lastPostId;
    }

    private static JSONArray getCommentsForPost(int postId) throws IOException {
        String url = BASE_URL + "/posts/" + postId + "/comments";
        HttpGet request = new HttpGet(url);
        HttpResponse response = httpClient.execute(request);

        JSONArray comments = new JSONArray();

        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String responseBody = EntityUtils.toString(entity);
                comments = new JSONArray(responseBody);
            }
        } finally {
            response.getEntity().getContent().close();
        }

        return comments;
    }

    private static void saveCommentsToFile(int userId, int postId, JSONArray comments) throws IOException {
        String fileName = "user-" + userId + "-post-" + postId + "-comments.json";
        FileWriter fileWriter = new FileWriter(fileName);

        try {
            fileWriter.write(comments.toString(2));  // Записуємо JSON у файл з відступами
        } finally {
            fileWriter.close();
        }
    }
}
