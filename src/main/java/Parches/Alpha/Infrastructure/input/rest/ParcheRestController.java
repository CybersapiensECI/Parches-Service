package Parches.Alpha.Infrastructure.input.rest;

import Parches.Alpha.Aplication.dto.CreateParcheCommand;
import Parches.Alpha.Aplication.dto.ParcheQueryFilter;
import Parches.Alpha.Aplication.ports.CreateParcheUseCase;
import Parches.Alpha.Aplication.ports.JoinParcheUseCase;
import Parches.Alpha.Aplication.ports.SearchAvailableParchesUseCase;
import Parches.Alpha.Domain.Model.Parche;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/parches")
@RequiredArgsConstructor
public class ParcheRestController {

    private final CreateParcheUseCase createParcheUseCase;
    private final SearchAvailableParchesUseCase searchAvailableParchesUseCase;
    private final JoinParcheUseCase joinParcheUseCase;

    @PostMapping
    public ResponseEntity<?> createParche(@RequestBody CreateParcheCommand command) {
        UUID parcheId = createParcheUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(java.util.Map.of("parcheId", parcheId));
    }

    @GetMapping
    public ResponseEntity<?> searchParches(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String place,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Integer availableSlots,
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        ParcheQueryFilter filter = new ParcheQueryFilter(category, place, date, availableSlots, query);
        List<Parche> parches = searchAvailableParchesUseCase.execute(filter, page, size);
        return ResponseEntity.ok(parches);
    }

    @PostMapping("/{parcheId}/join")
    public ResponseEntity<?> joinParche(@PathVariable UUID parcheId, @RequestBody JoinRequest request) {
        joinParcheUseCase.execute(parcheId, request.studentId());
        return ResponseEntity.ok(java.util.Map.of("message", "Te has unido exitosamente al parche."));
    }

    public record JoinRequest(UUID studentId) {}
}
