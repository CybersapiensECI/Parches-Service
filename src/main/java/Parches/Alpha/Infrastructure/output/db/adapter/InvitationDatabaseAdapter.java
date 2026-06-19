package Parches.Alpha.Infrastructure.output.db.adapter;

import Parches.Alpha.Domain.Model.Invitation;
import Parches.Alpha.Domain.spi.InvitationRepositorySPI;
import Parches.Alpha.Infrastructure.output.db.entity.InvitationEntity;
import Parches.Alpha.Infrastructure.output.db.mapper.InvitationMapper;
import Parches.Alpha.Infrastructure.output.db.repository.InvitationJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class InvitationDatabaseAdapter implements InvitationRepositorySPI {

    private final InvitationJpaRepository invitationJpaRepository;

    @Autowired
    public InvitationDatabaseAdapter(InvitationJpaRepository invitationJpaRepository) {
        this.invitationJpaRepository = invitationJpaRepository;
    }

    @Override
    public UUID save(Invitation invitation) {
        InvitationEntity entity = InvitationMapper.toEntity(invitation);
        InvitationEntity saved = invitationJpaRepository.save(entity);
        return saved.getId();
    }

    @Override
    public Optional<Invitation> findById(UUID id) {
        return invitationJpaRepository.findById(id).map(InvitationMapper::toDomain);
    }

    @Override
    public boolean existsPendingInvitation(UUID parcheId, UUID invitedId) {
        return invitationJpaRepository.existsByParcheIdAndInvitedIdAndStateIgnoreCase(parcheId, invitedId, "PENDING");
    }
}
