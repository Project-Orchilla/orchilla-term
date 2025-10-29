package software.siani.orchilla;

import com.google.gson.Gson;
import io.javalin.Javalin;
import io.javalin.json.JsonMapper;

import java.lang.reflect.Type;

public class MainRestAPI {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.jsonMapper(jsonMapper());
            config.showJavalinBanner = false;
        }).start(8080);
        app.post("/process", EngineHandler::processEngine);
    }

    private static JsonMapper jsonMapper() {
        return new JsonMapper() {
            private final Gson gson = new Gson();

            @Override
            public String toJsonString(Object object, Type type) {
                return gson.toJson(object);
            }

            @Override
            public <T> T fromJsonString(String json, Type targetType) {
                return gson.fromJson(json, targetType);
            }
        };
    }
}
