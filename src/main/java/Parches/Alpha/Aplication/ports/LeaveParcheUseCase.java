package Parches.Alpha.Aplication.ports;

import java.util.UUID;

public interface LeaveParcheUseCase {
    void execute(UUID parcheId, UUID studentId);
}
