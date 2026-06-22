package Parches.Alpha.Infrastructure.output.db.adapter;

import Parches.Alpha.Aplication.dto.ParcheQueryFilter;
import Parches.Alpha.Domain.Model.Parche;
import Parches.Alpha.Domain.spi.ParcheRepositorySPI;
import Parches.Alpha.Infrastructure.output.db.entity.ParcheEntity;
import Parches.Alpha.Infrastructure.output.db.mapper.ParcheMapper;
import Parches.Alpha.Infrastructure.output.db.repository.ParcheJpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ParcheDatabaseAdapter implements ParcheRepositorySPI {

    private final ParcheJpaRepository parcheJpaRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public ParcheDatabaseAdapter(ParcheJpaRepository parcheJpaRepository) {
        this.parcheJpaRepository = parcheJpaRepository;
    }

    @Override
    public UUID save(Parche parche) {
        ParcheEntity entity = ParcheMapper.toEntity(parche);
        if (parcheJpaRepository.existsById(entity.getId())) {
            entity.setNew(false);
        }
        ParcheEntity saved = parcheJpaRepository.save(entity);
        return saved.getId();
    }

    @Override
    public Optional<Parche> findById(UUID id) {
        return parcheJpaRepository.findById(id).map(ParcheMapper::toDomain);
    }

    @Override
    public List<Parche> findAllWithFilters(ParcheQueryFilter filter, int page, int size) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ParcheEntity> query = cb.createQuery(ParcheEntity.class);
        Root<ParcheEntity> root = query.from(ParcheEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        // Solo parches activos
        predicates.add(cb.equal(root.get("status"), "ACTIVE"));

        if (filter != null) {
            if (filter.category() != null && !filter.category().trim().isEmpty()) {
                predicates.add(cb.equal(root.get("category"), filter.category().toUpperCase()));
            }

            if (filter.place() != null && !filter.place().trim().isEmpty()) {
                predicates.add(cb.equal(root.get("lugar"), filter.place().toUpperCase()));
            }

            if (filter.date() != null) {
                predicates.add(cb.equal(root.get("date"), filter.date()));
            }

            if (filter.query() != null && !filter.query().trim().isEmpty()) {
                String likePattern = "%" + filter.query().toLowerCase() + "%";
                Predicate nameLike = cb.like(cb.lower(root.get("name")), likePattern);
                Predicate descLike = cb.like(cb.lower(root.get("description")), likePattern);
                predicates.add(cb.or(nameLike, descLike));
            }

            if (filter.availableSlots() != null) {
                Expression<Integer> sizeMembers = cb.size(root.get("members"));
                Expression<Integer> maximumQuota = root.get("maximumQuota");
                predicates.add(cb.greaterThanOrEqualTo(cb.diff(maximumQuota, sizeMembers), filter.availableSlots()));
            }
        }

        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(cb.asc(root.get("date")), cb.asc(root.get("hour")));

        TypedQuery<ParcheEntity> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(page * size);
        typedQuery.setMaxResults(size);

        return typedQuery.getResultList().stream()
                .map(ParcheMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public long countActiveParchesByStudentId(UUID studentId) {
        return parcheJpaRepository.countActiveParchesByStudentId(studentId);
    }
}
