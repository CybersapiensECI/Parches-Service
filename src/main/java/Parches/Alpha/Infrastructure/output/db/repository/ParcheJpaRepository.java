package Parches.Alpha.Infrastructure.output.db.repository;

import Parches.Alpha.Infrastructure.output.db.entity.ParcheEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ParcheJpaRepository extends JpaRepository<ParcheEntity, UUID> {

    @Query("SELECT COUNT(p) FROM ParcheEntity p JOIN p.members m WHERE m.studentId = :studentId AND p.status = 'ACTIVE'")
    long countActiveParchesByStudentId(@Param("studentId") UUID studentId);
}
