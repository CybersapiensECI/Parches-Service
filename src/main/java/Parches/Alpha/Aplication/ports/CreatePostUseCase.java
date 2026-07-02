package Parches.Alpha.Aplication.ports;

import java.util.UUID;

public interface CreatePostUseCase {
    void execute(UUID parcheId, UUID authorId, String text, String photoUrl);
}
