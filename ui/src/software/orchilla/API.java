package software.orchilla;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class API {
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();

    public static List<String> recognize(String text) throws IOException, InterruptedException {
        String url = "http://localhost:8001/recognize";

        Map<String, Object> payload = new HashMap<>();
        payload.put("text", text);

        String json = gson.toJson(payload);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("recognition response:\n" + response.body());

        JsonArray arr = JsonParser.parseString(response.body()).getAsJsonObject().getAsJsonArray("entities");

        List<String> entities = new ArrayList<>();
        if (arr != null) {
            for (JsonElement e : arr) {
                entities.add(e.getAsString());
            }
        }
        return entities;
    }


    public static List<String> decompose(String text) throws IOException, InterruptedException {
        String url = "http://localhost:8002/decompose";

        Map<String, Object> payload = new HashMap<>();
        payload.put("text", text);

        String json = gson.toJson(payload);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("decomposition response:\n" + response.body());

        JsonArray arr = JsonParser.parseString(response.body()).getAsJsonObject().getAsJsonArray("entities");

        List<String> entities = new ArrayList<>();
        if (arr != null) {
            for (JsonElement e : arr) {
                entities.add(e.getAsString());
            }
        }
        return entities;
    }

    public static String formalize(List<String> units) throws IOException, InterruptedException {
        String url = "http://localhost:8004/predict";

        Map<String, Object> payload = new HashMap<>();
        payload.put("units", units);

        String json = gson.toJson(payload);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("formalization response:\n" + response.body());

        return JsonParser.parseString(response.body()).getAsJsonObject().get("prediction").getAsString();
    }

    public static EngineOutputDTO process(String context, String operations) throws IOException, InterruptedException {
        String url = "http://localhost:8080/process";

        Map<String, Object> payload = new HashMap<>();
        payload.put("context", context);
        payload.put("operations", operations);

        String json = gson.toJson(payload);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("process response:\n" + response.body());
        return gson.fromJson(response.body(), EngineOutputDTO.class);
    }

}
