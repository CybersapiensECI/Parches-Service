package Parches.Alpha.Domain.spi;

import Parches.Alpha.Aplication.dto.ParcheQueryFilter;
import Parches.Alpha.Domain.Model.Parche;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ParcheRepositorySPI {
    UUID save(Parche parche);
    Optional<Parche> findById(UUID id);
    List<Parche> findAllWithFilters(ParcheQueryFilter filter, int page, int size);
    long countActiveParchesByStudentId(UUID studentId);
    List<Parche> findByStudentId(UUID studentId);
    Optional<Parche> findByPostId(UUID postId);
    Optional<Parche> findByCommentId(UUID commentId);
}