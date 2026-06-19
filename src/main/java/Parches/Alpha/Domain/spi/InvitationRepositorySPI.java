package Parches.Alpha.Domain.spi;

import Parches.Alpha.Domain.Model.Invitation;

import java.util.Optional;
import java.util.UUID;

public interface InvitationRepositorySPI {
    UUID save(Invitation invitation);
    Optional<Invitation> findById(UUID id);
    boolean existsPendingInvitation(UUID parcheId, UUID invitedId);
}