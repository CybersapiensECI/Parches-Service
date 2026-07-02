package Parches.Alpha.Aplication.ports;

import Parches.Alpha.Domain.Model.Member;
import java.util.List;
import java.util.UUID;

public interface GetParcheMembersUseCase {
    List<Member> execute(UUID parcheId);
}
