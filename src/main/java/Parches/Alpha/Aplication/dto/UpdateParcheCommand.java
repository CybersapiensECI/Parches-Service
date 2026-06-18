package Parches.Alpha.Aplication.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record UpdateParcheCommand(
        String name,
        String description,
        String place,
        String category,
        String type,
        LocalDate date,
        LocalTime hour,
        int maximumQuota
) {}
