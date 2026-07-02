package Parches.Alpha.Aplication.ports;

import Parches.Alpha.Domain.Model.Post;
import java.util.List;

public interface FindAllPostsUseCase {
    List<Post> execute();
}
