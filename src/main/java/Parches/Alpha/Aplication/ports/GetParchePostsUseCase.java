package Parches.Alpha.Aplication.ports;

import Parches.Alpha.Domain.Model.Post;
import java.util.List;
import java.util.UUID;

public interface GetParchePostsUseCase {
    List<Post> execute(UUID parcheId);
}
