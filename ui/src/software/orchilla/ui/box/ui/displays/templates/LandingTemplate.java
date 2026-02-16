package software.orchilla.ui.box.ui.displays.templates;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.ui.displays.UserMessage;
import org.jetbrains.annotations.NotNull;
import software.orchilla.EngineOutputDTO;
import software.orchilla.ui.box.UiBox;
import software.orchilla.ui.box.ui.displays.TimelineDisplay;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static software.orchilla.API.*;

public class LandingTemplate extends AbstractLandingTemplate<UiBox> {
    private static final List<String> phrases = List.of(
            "We'll start onboarding on the last Monday in March at 7 in the morning.",
            "The library closes in 40 minutes.",
            "We visited family on Christmas of the year 2021.",
            "We had our last meeting around 3 weeks ago.",
            "The contract renewal is due mid-next month.",
            "The morning check-in will be at 7:45 AM on the Monday of the last week of July.",
            "The software update came last weekend.",
            "Early this morning, the server went offline.",
            "This upcoming weekend, we’re heading to the coast.",
            "She’ll check in on the second Monday of next quarter.",
            "You’ll receive a calendar invite the day after the third Wednesday in August, around 4 PM.",
            "The deadline passed two hours ago.",
            "We're traveling on the 21st of December.",
            "Expect an alert after 10 PM on the final Sunday before the holiday break.",
            "He was here earlier today.",
            "She’ll be back on the morning of the first Thursday in August.",
            "The exhibition runs through July.",
            "The team lunch is scheduled for the first Friday after project kickoff, at 12:30 PM.",
            "It all happened in the fall of 2012.",
            "The project launch is scheduled for next week.",
            "Back in 2015, we had a completely different strategy.",
            "The conference starts at 9:15AM.",
            "He started his job in early March.",
            "On the eve of the final exam, I pulled an all-nighter.",
            "Check back on the second Wednesday following your first login, at 5 PM.",
            "At the stroke of midnight, the new law took effect."
    );


    public LandingTemplate(UiBox box) {
		super(box);
	}



    @Override
    public void refresh() {
        super.refresh();
        this.titleField.value("Orchilla's Playground");
        this.memoBlock.cssSelectors(Set.of("orchilla-memo"));
        setRandomPhrase();
    }

    private void setRandomPhrase() {
        this.memoField.value(randomPhrase());
    }

    private static String randomPhrase() {
        Random random = new Random();
        return phrases.get(random.nextInt(phrases.size()));
    }

    @Override
    public void init() {
        super.init();
        this.timelineStamp.display(new TimelineDisplay(box()));
        evaluate.onExecute(event -> evaluate());
    }

    private void evaluate() {
        String content = this.memoField.value();
        Map<String, EngineOutputDTO> result = getTemporalEvents(content);
        refreshTemporalEvents(result);
        refreshTimeline(result);
    }

    private void refreshTimeline(Map<String, EngineOutputDTO> events) {
        TimelineDisplay display = timelineStamp.display();
        display.events(events);
        display.refresh();
    }

    private void refreshTemporalEvents(Map<String, EngineOutputDTO> events) {
        StringBuilder builder = new StringBuilder();
        for (String key : events.keySet()) builder.append(key).append(": ").append(events.get(key)).append("\n");
        this.textField.value(builder.toString());
        this.textField.refresh();
    }

    private Map<String, EngineOutputDTO> getTemporalEvents(String content) {
        Map<String, EngineOutputDTO> result = new HashMap<>();
        try {
            List<String> entities = recognize(content);
            for (String entity : entities) {
                List<String> decompositionResult = decompose(entity);
                String formalizationResult = formalize(decompositionResult);
                EngineOutputDTO engineOutput = process(context(), formalizationResult);
                result.put(entity, engineOutput);
            }
        } catch (Throwable e) {
            Logger.error(e);
            this.notifyUser(e.getMessage(), UserMessage.Type.Error);
        }
        return result;
    }

    @NotNull
    private String context() {
        Instant instant = this.contextDate.value() != null
                ? this.contextDate.value()
                : Instant.now();
        return DateTimeFormatter.ofPattern("yyyyMMdd'T'HH:mm:ss")
                .withZone(ZoneId.systemDefault())
                .format(instant);
    }

}