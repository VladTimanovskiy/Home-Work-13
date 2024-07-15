package module13.Task1;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JsonPlaceholderClient {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/users";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(JsonPlaceholderClient.class);

    public static void main(String[] args) {
        try {
            // Create a new user
            String newUserJson = "{\"name\": \"John Doe\", \"username\": \"johndoe\", \"email\": \"johndoe@example.com\"}";
            JsonNode createdUser = createUser(newUserJson);
            logger.info("Created User: " + createdUser);

            // Update a user
            String updatedUserJson = "{\"id\": 1, \"name\": \"John Smith\", \"username\": \"johnsmith\", \"email\": \"johnsmith@example.com\"}";
            JsonNode updatedUser = updateUser(updatedUserJson);
            logger.info("Updated User: " + updatedUser);

            // Delete a user
            int userIdToDelete = 1;
            boolean isDeleted = deleteUser(userIdToDelete);
            logger.info("User Deleted: " + isDeleted);

            // Get all users
            JsonNode allUsers = getAllUsers();
            logger.info("All Users: " + allUsers);

            // Get user by ID
            int userId = 1;
            JsonNode userById = getUserById(userId);
            logger.info("User by ID: " + userById);

            // Get user by username
            String username = "Bret";
            JsonNode userByUsername = getUserByUsername(username);
            logger.info("User by Username: " + userByUsername);

        } catch (IOException e) {
            logger.error("Error occurred", e);
        }
    }

    public static JsonNode createUser(String userJson) throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(BASE_URL);
            StringEntity entity = new StringEntity(userJson);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse response = client.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            String responseString = EntityUtils.toString(responseEntity);
            return objectMapper.readTree(responseString);
        }
    }

    public static JsonNode updateUser(String userJson) throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            JsonNode userNode = objectMapper.readTree(userJson);
            int userId = userNode.get("id").asInt();
            HttpPut httpPut = new HttpPut(BASE_URL + "/" + userId);
            StringEntity entity = new StringEntity(userJson);
            httpPut.setEntity(entity);
            httpPut.setHeader("Accept", "application/json");
            httpPut.setHeader("Content-type", "application/json");

            HttpResponse response = client.execute(httpPut);
            HttpEntity responseEntity = response.getEntity();
            String responseString = EntityUtils.toString(responseEntity);
            return objectMapper.readTree(responseString);
        }
    }

    public static boolean deleteUser(int userId) throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpDelete httpDelete = new HttpDelete(BASE_URL + "/" + userId);
            HttpResponse response = client.execute(httpDelete);
            int statusCode = response.getStatusLine().getStatusCode();
            return statusCode >= 200 && statusCode < 300;
        }
    }

    public static JsonNode getAllUsers() throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(BASE_URL);
            HttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity);
            return objectMapper.readTree(responseString);
        }
    }

    public static JsonNode getUserById(int userId) throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(BASE_URL + "/" + userId);
            HttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity);
            return objectMapper.readTree(responseString);
        }
    }

    public static JsonNode getUserByUsername(String username) throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(BASE_URL + "?username=" + username);
            HttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity);
            return objectMapper.readTree(responseString);
        }
    }
}
