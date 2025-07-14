package software.siani.orchilla.quassar;

import io.quassar.ulpgc.orchilla.Event;
import io.quassar.ulpgc.orchilla.ModelParser;
import io.quassar.ulpgc.orchilla.OrchillaGraph;
import io.quassar.ulpgc.orchilla.TemporalExpression;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class QuassarParser {
    public HashMap<String, String> parse(URL url) throws IOException {
        ModelParser.Model model = ModelParser.loadFromURL(url);
        OrchillaGraph graph = model.graph();
        HashMap<String, String> result = new HashMap<>();
        processEvents(graph.eventList(), result);
        processTemporalExpressions(graph.temporalExpressionList(), result);
        return result;
    }

    private static void processEvents(List<Event> events, HashMap<String, String> result) {
        for (Event event : events)
            result.put(event.name(), event.context() + ">>" + serialize(processTemporalExpression(event.temporalExpression())));
    }

    private static void processTemporalExpressions(List<TemporalExpression> temporalExpressions, HashMap<String, String> result) {
        String defaultName = "result";
        for (int i = 0; i < temporalExpressions.size(); i++)
            result.put(defaultName + i, "now" + ">>" + serialize(processTemporalExpression(temporalExpressions.get(i))));
    }

    private static List<TemporalExpression> processTemporalExpression(TemporalExpression temporalExpression) {
        ArrayList<TemporalExpression> result = new ArrayList<>();
        processTemporalExpressionHelper(temporalExpression, result);
        return result;
    }

    private static void processTemporalExpressionHelper(TemporalExpression temporalExpression, ArrayList<TemporalExpression> result) {
        result.add(temporalExpression);
        if (temporalExpression.temporalExpression() == null) return;
        processTemporalExpressionHelper(temporalExpression.temporalExpression(), result);
    }

    private static String serialize(List<TemporalExpression> temporalExpressions) {
        return temporalExpressions.stream().map(QuassarParser::serialize).collect(Collectors.joining(">>"));
    }

    private static String serialize(TemporalExpression temporalExpression) {
        List<?> parameters = temporalExpression.core$().variables().getOrDefault("parameters", null);
        if (parameters == null) return temporalExpression.getClass().getSimpleName().toLowerCase();
        return temporalExpression.getClass().getSimpleName().toLowerCase() + " " + parameters.getFirst().toString();
    }
}
