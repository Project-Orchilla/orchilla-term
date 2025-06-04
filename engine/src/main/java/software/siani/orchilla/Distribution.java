package software.siani.orchilla;

import java.time.LocalDateTime;

public interface Distribution {
    Distribution between(LocalDateTime start, LocalDateTime end);
}
