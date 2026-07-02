package Parches.Alpha.Infrastructure.output.db.adapter;

import Parches.Alpha.Domain.Model.Post;
import Parches.Alpha.Domain.spi.PostRepositorySPI;
import Parches.Alpha.Infrastructure.output.db.mapper.ParcheMapper;
import Parches.Alpha.Infrastructure.output.db.repository.PostJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostDatabaseAdapter implements PostRepositorySPI {

    private final PostJpaRepository postJpaRepository;

    @Autowired
    public PostDatabaseAdapter(PostJpaRepository postJpaRepository) {
        this.postJpaRepository = postJpaRepository;
    }

    @Override
    public List<Post> findAllOrderByCreatedAtDesc() {
        return postJpaRepository.findAllOrderByCreatedAtDesc().stream()
                .map(ParcheMapper::toPostDomain)
                .collect(Collectors.toList());
    }
}
