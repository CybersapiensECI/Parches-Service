package Parches.Alpha.Domain.spi;

import Parches.Alpha.Domain.Model.Post;
import java.util.List;

public interface PostRepositorySPI {
    List<Post> findAllOrderByCreatedAtDesc();
}
