package software.orchilla;

import com.google.gson.*;
import es.monentia.gal2.api.box.Gal2ModelsAccessor;
import es.monentia.gal2.api.box.schemas.ModelExecutionRequest;
import es.monentia.gal2.api.box.schemas.ModelExecutionResponse;
import es.monentia.gal2.api.box.schemas.TimeoutHandler;
import io.intino.alexandria.exceptions.BadRequest;
import io.intino.alexandria.exceptions.InternalServerError;
import io.intino.alexandria.exceptions.NotFound;
import io.intino.alexandria.exceptions.Unauthorized;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class API {
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final String token = "app5ab0bf28179841b0b07524a4b4de90e6";
    private static final Gal2ModelsAccessor accessor;
    private static final Gson gson = new Gson();

    static {
        try {
            accessor = new Gal2ModelsAccessor(URI.create("http://192.168.42.217:31907").toURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> recognize(String text) throws BadRequest, Unauthorized, InternalServerError, NotFound {
        ModelExecutionRequest request = new ModelExecutionRequest()
                .name("recognizer")
                .namespace("usr-externa-orchilla")
                .input(List.of(text))
                .timeoutHandler(new TimeoutHandler().readTimeoutSeconds(100).connectTimeoutSeconds(100))
                .headers(Map.of("Content-Type", "application/json"));
        ModelExecutionResponse modelExecutionResponse = accessor.postExecuteModel(token, request, List.of());
        return gson.fromJson(modelExecutionResponse.output(), JsonObject.class)
                .getAsJsonArray("entities")
                .asList()
                .stream()
                .map(e -> e.toString().substring(1, e.toString().length() - 1))
                .toList();
    }

    public static List<String> decompose(String text) throws BadRequest, Unauthorized, InternalServerError, NotFound {
        ModelExecutionRequest request = new ModelExecutionRequest()
                .name("decomposer")
                .namespace("usr-externa-orchilla")
                .input(List.of(text))
                .timeoutHandler(new TimeoutHandler().readTimeoutSeconds(100).connectTimeoutSeconds(100))
                .headers(Map.of("Content-Type", "application/json"));
        ModelExecutionResponse modelExecutionResponse = accessor.postExecuteModel(token, request, List.of());
        return gson.fromJson(modelExecutionResponse.output(), JsonObject.class)
                .getAsJsonArray("entities")
                .asList()
                .stream()
                .map(e -> e.toString().substring(1, e.toString().length() - 1))
                .toList();
    }

    public static String formalize(List<String> units) throws BadRequest, Unauthorized, InternalServerError, NotFound {
        ModelExecutionRequest request = new ModelExecutionRequest()
                .name("formalizer")
                .namespace("usr-externa-orchilla")
                .input(List.of(units.stream().map(u -> "\"" + u + "\"").toList().toString()))
                .timeoutHandler(new TimeoutHandler().readTimeoutSeconds(100).connectTimeoutSeconds(100))
                .headers(Map.of("Content-Type", "application/json"));
        ModelExecutionResponse modelExecutionResponse = accessor.postExecuteModel(token, request, List.of());
        return gson.fromJson(modelExecutionResponse.output(), JsonObject.class)
                .get("prediction")
                .getAsString();
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
