package Parches.Alpha.Infrastructure.output.db.repository;

import Parches.Alpha.Infrastructure.output.db.entity.InvitationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface InvitationJpaRepository extends JpaRepository<InvitationEntity, UUID> {
    boolean existsByParcheIdAndInvitedIdAndStateIgnoreCase(UUID parcheId, UUID invitedId, String state);
}
