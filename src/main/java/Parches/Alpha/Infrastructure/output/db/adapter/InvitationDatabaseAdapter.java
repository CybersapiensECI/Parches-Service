package Parches.Alpha.Infrastructure.output.db.adapter;

import Parches.Alpha.Domain.Model.Invitation;
import Parches.Alpha.Domain.spi.InvitationRepositorySPI;
import Parches.Alpha.Infrastructure.output.db.entity.InvitationEntity;
import Parches.Alpha.Infrastructure.output.db.mapper.InvitationMapper;
import Parches.Alpha.Infrastructure.output.db.repository.InvitationJpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class InvitationDatabaseAdapter implements InvitationRepositorySPI {

    private final InvitationJpaRepository invitationJpaRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public InvitationDatabaseAdapter(InvitationJpaRepository invitationJpaRepository) {
        this.invitationJpaRepository = invitationJpaRepository;
    }

    @Override
    public UUID save(Invitation invitation) {
        InvitationEntity entity = InvitationMapper.toEntity(invitation);
        if (invitationJpaRepository.existsById(entity.getId())) {
            entity.setNew(false);
        }
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
