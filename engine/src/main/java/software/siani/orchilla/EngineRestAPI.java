package software.siani.orchilla;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import software.siani.orchilla.model.TemporalExpression;
import systems.intino.datamarts.subjectstore.SubjectStore;

import java.io.File;
import java.io.IOException;

public class EngineRestAPI {
    private static final SubjectStore subjectStore;

    static {
        try {
            subjectStore = new SubjectStore(new File("subjectstore.triplets"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/process")
    public ResponseEntity<TemporalExpression> processEngine(@RequestBody InputDTO payload) {
        TemporalExpression temporalExpression = new TemporalExpression.Builder().with(subjectStore).build(payload.context() + ">>" + payload.operations());
        return ResponseEntity.ok(temporalExpression);
    }
}
