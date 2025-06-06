package software.siani.orchilla;

import com.google.gson.Gson;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import software.siani.orchilla.model.Distribution;
import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalExpression;
import software.siani.orchilla.model.TemporalTag;
import systems.intino.datamarts.subjectstore.SubjectStore;

import java.io.File;
import java.io.IOException;

@RestController
public class EngineRestAPI {
    private static final SubjectStore subjectStore;

    static {
        try {
            subjectStore = new SubjectStore(new File("subjectstore.triplets"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping(
            path = "/process",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> processEngine(@RequestBody software.siani.orchilla.InputDTO payload) {
        TemporalExpression temporalExpression = new TemporalExpression.Builder()
                .with(subjectStore)
                .build(payload.context() + ">>" + payload.operations());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (temporalExpression.context() == null) {
            String innerJson = String.format(
                    "{\"context\":\"???\",\"operations\":%s}",
                    temporalExpression.operators().stream().map(o -> "\"" + o.toString() + "\"").toList()
            );
            return new ResponseEntity<>(innerJson, headers, HttpStatus.OK);
        }
        TemporalTag result = temporalExpression.solve();
        return new ResponseEntity<>(new Gson().toJson(new TemporalTagPojo(result)), headers, HttpStatus.OK);
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
