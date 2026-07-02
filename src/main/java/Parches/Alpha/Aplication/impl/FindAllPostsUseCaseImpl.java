package Parches.Alpha.Aplication.impl;

import Parches.Alpha.Aplication.ports.FindAllPostsUseCase;
import Parches.Alpha.Domain.Model.Post;
import Parches.Alpha.Domain.spi.PostRepositorySPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class FindAllPostsUseCaseImpl implements FindAllPostsUseCase {

    private final PostRepositorySPI postRepository;

    @Autowired
    public FindAllPostsUseCaseImpl(PostRepositorySPI postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<Post> execute() {
        return postRepository.findAllOrderByCreatedAtDesc();
    }
}
