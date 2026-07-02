package Parches.Alpha.Aplication.ports;

import java.util.UUID;

public interface CreateCommentUseCase {
    void execute(UUID postId, UUID authorId, String text);
}
