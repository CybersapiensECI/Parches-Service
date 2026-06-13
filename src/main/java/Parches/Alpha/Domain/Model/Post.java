package Parches.Alpha.Domain.Model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Post {
    private final UUID id;
    private String text;
    private String photoUrl;
    private final LocalDateTime createdAt;
}
