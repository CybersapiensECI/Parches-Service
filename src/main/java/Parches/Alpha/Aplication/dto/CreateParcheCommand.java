package Parches.Alpha.Aplication.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record CreateParcheCommand(
        String name,
        String description,
        String place,
        String category,
        String type,           // PUBLIC / PRIVATE
        LocalDate date,
        LocalTime hour,
        int maximumQuota,
        UUID creatorStudentId, // Extraído del estudiante autenticado
        UUID eventId          // Opcional
) {}
