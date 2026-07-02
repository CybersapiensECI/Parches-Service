package Parches.Alpha.Aplication.ports;

import java.util.UUID;

public interface ToggleCommentReactionUseCase {
    void execute(UUID commentId, UUID studentId);
}
