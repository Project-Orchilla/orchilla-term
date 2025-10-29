package software.siani.orchilla;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import software.siani.orchilla.model.Distribution;
import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalExpression;
import software.siani.orchilla.model.TemporalTag;
import systems.intino.datamarts.subjectstore.SubjectStore;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class EngineHandler {
    private static final SubjectStore subjectStore;

    static {
        try {
            subjectStore = new SubjectStore(new File("subjectstore.triplets"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void processEngine(Context ctx) {
        Gson gson = new Gson();
        InputDTO payload = gson.fromJson(ctx.body(), InputDTO.class);

        TemporalExpression temporalExpression = new TemporalExpression.Builder()
                .with(subjectStore)
                .build(payload.context() + ">>" + payload.operations());

        if (temporalExpression.context() == null) {
            String innerJson = gson.toJson(Map.of(
                    "context", "???",
                    "operations", temporalExpression.operators().stream().map(Object::toString).toList()
            ));
            ctx.status(HttpStatus.OK).json(innerJson);
            return;
        }

        TemporalTag result = temporalExpression.solve();
        TemporalTagPojo pojo = new TemporalTagPojo(result);
        ctx.status(HttpStatus.OK).json(pojo);
    }

    public static class TemporalTagPojo {
        public String head;
        public String end;
        public Distribution distribution;
        public long duration;
        public Period period;

        public TemporalTagPojo(TemporalTag tag) {
            this.head = tag.head().toString();
            this.end = tag.tail().toString();
            this.distribution = tag.distribution();
            this.duration = tag.duration();
            this.period = tag.period();
        }
    }
}
