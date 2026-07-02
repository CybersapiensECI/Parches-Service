package Parches.Alpha.Aplication.ports;

import java.util.UUID;

public interface TogglePostReactionUseCase {
    void execute(UUID postId, UUID studentId);
}
