package software.siani.orchilla.model;

import java.time.LocalDateTime;

public interface Distribution {
    Distribution between(LocalDateTime start, LocalDateTime end);
}
