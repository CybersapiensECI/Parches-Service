package Parches.Alpha.Infrastructure.output.db.repository;

import Parches.Alpha.Infrastructure.output.db.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface PostJpaRepository extends JpaRepository<PostEntity, UUID> {
    
    @Query("SELECT p FROM PostEntity p ORDER BY p.createdAt DESC")
    List<PostEntity> findAllOrderByCreatedAtDesc();
}
