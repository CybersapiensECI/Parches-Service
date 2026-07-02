package Parches.Alpha.Aplication.ports;

import java.util.UUID;

public interface JoinParcheUseCase {
    void execute(UUID parcheId, UUID studentId);
}
