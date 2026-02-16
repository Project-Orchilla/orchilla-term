package software.orchilla.ui.box.ui.displays;

import software.orchilla.EngineOutputDTO;
import software.orchilla.ui.box.schemas.*;
import software.orchilla.ui.box.UiBox;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class TimelineDisplay extends AbstractTimelineDisplay<UiBox> {
    private List<TimelineDTO> dtos;

    public TimelineDisplay(UiBox box) {
        super(box);
        this.dtos = null;
    }

    public void updateTimeline(List<TimelineDTO> value) {
        super.refresh();
        if (value == null || value.isEmpty()) {
            notifier.refresh(defaultTimeline());
        } else {
            this.dtos = value;
            notifier.refresh(value);
        }
    }

    @Override
    public void refresh() {
        super.refresh();
        if (this.dtos == null || this.dtos.isEmpty()) {
            // notifier.refresh(defaultTimeline());
        } else {
            notifier.refresh(this.dtos);
        }
    }

    public List<TimelineDTO> defaultTimeline() {
        List<TimelineDTO> defaults = new ArrayList<>();

        defaults.add(new TimelineDTO()
                .x("2024-01-01")
                .name("System Initialization")
                .label("Startup")
                .description("The system started running for the first time."));

        defaults.add(new TimelineDTO()
                .x("2024-02-15")
                .name("First Release")
                .label("v1.0")
                .description("Initial release to production."));

        defaults.add(new TimelineDTO()
                .x("2024-04-10")
                .name("Feature Expansion")
                .label("v1.2")
                .description("Added new user management features."));

        defaults.add(new TimelineDTO()
                .x("2024-06-20")
                .name("Maintenance Update")
                .label("v1.3")
                .description("Security and performance improvements."));

        defaults.add(new TimelineDTO()
                .x("2024-09-05")
                .name("Major Upgrade")
                .label("v2.0")
                .description("Complete redesign of the core architecture."));

        return defaults;
    }

    public void events(Map<String, EngineOutputDTO> events) {
        ArrayList<TimelineDTO> result = new ArrayList<>();
        events.keySet().forEach(k -> result.addAll(parseToTimeLineDTO(k, events.get(k))));
        this.dtos = result;
    }

    private List<TimelineDTO> parseToTimeLineDTO(String name, EngineOutputDTO engineOutputDTO) {
        return List.of(new TimelineDTO()
                        .x(engineOutputDTO.head().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                        .name(name)
                        .label(engineOutputDTO.head().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        .description(name),
                       new TimelineDTO()
                        .x(engineOutputDTO.end().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                        .name(name)
                        .label(engineOutputDTO.end().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        .description(name)
        );
    }
}
