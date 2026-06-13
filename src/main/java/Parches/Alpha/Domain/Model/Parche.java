package Parches.Alpha.Domain.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class Parche {
    private final UUID id;
    private String name;
    private String description;
    private Places place;
    private ParcheCategory category;
    private ParcheType type;
    private LocalDate date;
    private LocalTime hour;
    private int maxiumQuota;
    private ParcheStatus status;
    private final UUID creatorStudentId;
    private final LocalDateTime creationDate;
    private boolean allowedMemberInvitation;
    private UUID eventId;
    private final List<Member> members;
    private final List<Invitation> invitations;
    private final List<Post> posts;

}
