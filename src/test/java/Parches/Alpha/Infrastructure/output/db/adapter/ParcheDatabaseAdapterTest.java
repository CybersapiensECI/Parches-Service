package Parches.Alpha.Infrastructure.output.db.adapter;

import Parches.Alpha.Aplication.dto.ParcheQueryFilter;
import Parches.Alpha.Domain.Enums.ParcheStatus;
import Parches.Alpha.Domain.Model.Parche;
import Parches.Alpha.Infrastructure.output.db.entity.ParcheEntity;
import Parches.Alpha.Infrastructure.output.db.repository.ParcheJpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ParcheDatabaseAdapterTest {

    @Mock
    private ParcheJpaRepository parcheJpaRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private ParcheDatabaseAdapter parcheDatabaseAdapter;

    private UUID parcheId;
    private Parche parcheDomain;
    private ParcheEntity parcheEntity;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(parcheDatabaseAdapter, "entityManager", entityManager);
        parcheId = UUID.randomUUID();
        parcheDomain = Parche.builder()
                .id(parcheId)
                .name("Futbol")
                .status(ParcheStatus.ACTIVE)
                .maximumQuota(10)
                .members(new ArrayList<>())
                .build();
        parcheEntity = ParcheEntity.builder()
                .id(parcheId)
                .name("Futbol")
                .status("ACTIVE")
                .maximumQuota(10)
                .members(new ArrayList<>())
                .build();
    }

    @Test
    void testSaveNewParche() {
        when(parcheJpaRepository.existsById(parcheId)).thenReturn(false);
        when(parcheJpaRepository.save(any(ParcheEntity.class))).thenReturn(parcheEntity);

        UUID result = parcheDatabaseAdapter.save(parcheDomain);

        assertEquals(parcheId, result);
        verify(parcheJpaRepository).save(any(ParcheEntity.class));
    }

    @Test
    void testSaveExistingParche() {
        when(parcheJpaRepository.existsById(parcheId)).thenReturn(true);
        when(parcheJpaRepository.save(any(ParcheEntity.class))).thenReturn(parcheEntity);

        UUID result = parcheDatabaseAdapter.save(parcheDomain);

        assertEquals(parcheId, result);
        verify(parcheJpaRepository).save(any(ParcheEntity.class));
    }

    @Test
    void testFindById() {
        when(parcheJpaRepository.findById(parcheId)).thenReturn(Optional.of(parcheEntity));

        Optional<Parche> result = parcheDatabaseAdapter.findById(parcheId);

        assertTrue(result.isPresent());
        assertEquals(parcheId, result.get().getId());
    }

    @Test
    void testCountActiveParchesByStudentId() {
        UUID studentId = UUID.randomUUID();
        when(parcheJpaRepository.countActiveParchesByStudentId(studentId)).thenReturn(3L);

        long result = parcheDatabaseAdapter.countActiveParchesByStudentId(studentId);

        assertEquals(3, result);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testFindAllWithFilters() {
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        CriteriaQuery<ParcheEntity> query = mock(CriteriaQuery.class);
        Root<ParcheEntity> root = mock(Root.class);
        TypedQuery<ParcheEntity> typedQuery = mock(TypedQuery.class);
        Predicate predicate = mock(Predicate.class);
        Path<Object> path = mock(Path.class);
        Expression<Integer> sizeExpr = mock(Expression.class);
        Expression<Integer> diffExpr = mock(Expression.class);

        when(entityManager.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(ParcheEntity.class)).thenReturn(query);
        when(query.from(ParcheEntity.class)).thenReturn(root);

        when(root.get(anyString())).thenReturn(path);
        when(cb.equal(any(), any())).thenReturn(predicate);
        when(cb.or(any(Predicate[].class))).thenReturn(predicate);
        when(cb.like(any(), anyString())).thenReturn(predicate);
        when(cb.lower(any())).thenReturn(mock(Expression.class));

        when(cb.size(any(Expression.class))).thenReturn(sizeExpr);
        when(cb.diff(any(Expression.class), any(Expression.class))).thenReturn(diffExpr);
        when(cb.greaterThanOrEqualTo(any(Expression.class), any(Integer.class))).thenReturn(predicate);

        when(entityManager.createQuery(query)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.singletonList(parcheEntity));

        ParcheQueryFilter filter = new ParcheQueryFilter(
                "SPORTS", "SOCCER_COURT_1", LocalDate.now(), 5, "Futbol"
        );

        java.util.List<Parche> result = parcheDatabaseAdapter.findAllWithFilters(filter, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(parcheId, result.get(0).getId());
    }
}
