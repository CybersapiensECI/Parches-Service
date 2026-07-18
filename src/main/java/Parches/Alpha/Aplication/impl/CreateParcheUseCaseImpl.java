package Parches.Alpha.Aplication.impl;

import Parches.Alpha.Aplication.dto.CreateParcheCommand;
import Parches.Alpha.Aplication.exception.MaxActiveParchesException;
import Parches.Alpha.Aplication.exception.ParcheFullException;
import Parches.Alpha.Aplication.ports.CreateParcheUseCase;
import Parches.Alpha.Domain.Enums.*;
import Parches.Alpha.Domain.Model.Parche;
import Parches.Alpha.Domain.spi.ParcheRepositorySPI;
import Parches.Alpha.Infrastructure.output.messaging.ParcheEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
@Transactional
public class CreateParcheUseCaseImpl implements CreateParcheUseCase {

    private final ParcheRepositorySPI parcheRepository;
    private final ParcheEventPublisher eventPublisher;

    @Autowired
    public CreateParcheUseCaseImpl(ParcheRepositorySPI parcheRepository, ParcheEventPublisher eventPublisher) {
        this.parcheRepository = parcheRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public UUID execute(CreateParcheCommand command) {
        long activeCount = parcheRepository.countActiveParchesByStudentId(command.creatorStudentId());
        if (activeCount >= 5) {
            throw new MaxActiveParchesException("El estudiante ya superó el máximo de 5 parches activos.");
        }

        if (command.maximumQuota() < 2 || command.maximumQuota() > 30) {
            throw new ParcheFullException("Cupo inválido. El cupo debe estar entre 2 y 30.");
        }

        Parche parche = Parche.builder()
                .id(UUID.randomUUID())
                .name(command.name())
                .description(command.description())
                .place(Places.valueOf(command.place().toUpperCase()))
                .category(ParcheCategory.valueOf(command.category().toUpperCase()))
                .type(ParcheType.valueOf(command.type().toUpperCase()))
                .date(command.date())
                .hour(command.hour())
                .maximumQuota(command.maximumQuota())
                .status(ParcheStatus.ACTIVE)
                .creatorStudentId(command.creatorStudentId())
                .creationDate(LocalDateTime.now())
                .allowedMemberInvitation(true)
                .eventId(command.eventId())
                .members(new ArrayList<>())
                .posts(new ArrayList<>())
                .invitations(new ArrayList<>())
                .build();

        parche.addMember(command.creatorStudentId(), MemberRole.CREATOR);

        UUID savedId = parcheRepository.save(parche);
        eventPublisher.publishParcheCreated(savedId, command.creatorStudentId(), parche.getCategory().name());
        return savedId;
    }
}
