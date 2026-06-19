package Parches.Alpha;

import Parches.Alpha.Aplication.dto.CreateParcheCommand;
import Parches.Alpha.Aplication.dto.SendInvitationCommand;
import Parches.Alpha.Aplication.exception.ActionNotAllowedException;
import Parches.Alpha.Aplication.exception.MaxActiveParchesException;
import Parches.Alpha.Aplication.exception.ParcheFullException;
import Parches.Alpha.Aplication.ports.*;
import Parches.Alpha.Domain.Model.Parche;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class DemoApplicationTests {

    @Autowired
    private CreateParcheUseCase createParcheUseCase;

    @Autowired
    private GetParcheDetailUseCase getParcheDetailUseCase;

    @Autowired
    private JoinParcheUseCase joinParcheUseCase;

    @Autowired
    private LeaveParcheUseCase leaveParcheUseCase;

    @Autowired
    private SendInvitationUseCase sendInvitationUseCase;

    @Autowired
    private AcceptInvitationUseCase acceptInvitationUseCase;

    @Autowired
    private RejectInvitationUseCase rejectInvitationUseCase;

    @Test
    void contextLoads() {
        assertNotNull(createParcheUseCase);
    }

    @Test
    void testCreateParcheAndGetDetail() {
        UUID studentId = UUID.randomUUID();
        CreateParcheCommand command = new CreateParcheCommand(
                "Futbol 8",
                "Partido de futbol",
                "SOCCER_COURT_1",
                "SPORTS",
                "PUBLIC",
                LocalDate.now().plusDays(2),
                LocalTime.of(16, 0),
                10,
                studentId,
                null
        );

        UUID parcheId = createParcheUseCase.execute(command);
        assertNotNull(parcheId);

        Parche details = getParcheDetailUseCase.execute(parcheId);
        assertEquals("Futbol 8", details.getName());
        assertEquals(studentId, details.getCreatorStudentId());
        assertEquals(1, details.getMembers().size());
        assertTrue(details.isStudentMember(studentId));
    }

    @Test
    void testMaxActiveParchesLimit() {
        UUID studentId = UUID.randomUUID();
        for (int i = 0; i < 5; i++) {
            CreateParcheCommand command = new CreateParcheCommand(
                    "Parche " + i,
                    "Description " + i,
                    "COLISEO",
                    "SPORTS",
                    "PUBLIC",
                    LocalDate.now().plusDays(2),
                    LocalTime.of(16, 0),
                    10,
                    studentId,
                    null
            );
            createParcheUseCase.execute(command);
        }

        CreateParcheCommand command6 = new CreateParcheCommand(
                "Parche 6",
                "Description 6",
                "COLISEO",
                "SPORTS",
                "PUBLIC",
                LocalDate.now().plusDays(2),
                LocalTime.of(16, 0),
                10,
                studentId,
                null
        );

        assertThrows(MaxActiveParchesException.class, () -> createParcheUseCase.execute(command6));
    }

    @Test
    void testJoinAndLeaveParche() {
        UUID captainId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();

        UUID parcheId = createParcheUseCase.execute(new CreateParcheCommand(
                "Cine ECI",
                "Ver pelicula",
                "BUILDING_A",
                "CINEMA",
                "PUBLIC",
                LocalDate.now().plusDays(2),
                LocalTime.of(18, 0),
                2,
                captainId,
                null
        ));

        joinParcheUseCase.execute(parcheId, studentId);
        Parche details = getParcheDetailUseCase.execute(parcheId);
        assertEquals(2, details.getMembers().size());
        assertTrue(details.isStudentMember(studentId));

        UUID extraStudentId = UUID.randomUUID();
        assertThrows(ParcheFullException.class, () -> joinParcheUseCase.execute(parcheId, extraStudentId));

        assertThrows(ActionNotAllowedException.class, () -> leaveParcheUseCase.execute(parcheId, captainId));

        leaveParcheUseCase.execute(parcheId, studentId);
        details = getParcheDetailUseCase.execute(parcheId);
        assertEquals(1, details.getMembers().size());
        assertFalse(details.isStudentMember(studentId));
    }

    @Test
    void testPrivateParcheRequiresInvitation() {
        UUID captainId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();

        UUID parcheId = createParcheUseCase.execute(new CreateParcheCommand(
                "Estudio Privado",
                "Grupo selecto",
                "BUILDING_C",
                "STUDIES",
                "PRIVATE",
                LocalDate.now().plusDays(2),
                LocalTime.of(10, 0),
                5,
                captainId,
                null
        ));

        assertThrows(ActionNotAllowedException.class, () -> joinParcheUseCase.execute(parcheId, studentId));

        UUID invitationId = sendInvitationUseCase.execute(new SendInvitationCommand(parcheId, captainId, studentId));
        assertNotNull(invitationId);

        acceptInvitationUseCase.execute(invitationId, studentId);

        Parche details = getParcheDetailUseCase.execute(parcheId);
        assertEquals(2, details.getMembers().size());
        assertTrue(details.isStudentMember(studentId));
    }
}
