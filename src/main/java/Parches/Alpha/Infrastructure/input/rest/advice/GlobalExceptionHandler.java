package Parches.Alpha.Infrastructure.input.rest.advice;

import Parches.Alpha.Aplication.exception.ActionNotAllowedException;
import Parches.Alpha.Aplication.exception.DuplicateInvitationException;
import Parches.Alpha.Aplication.exception.InvalidStateTransitionException;
import Parches.Alpha.Aplication.exception.MaxActiveParchesException;
import Parches.Alpha.Aplication.exception.ParcheFullException;
import Parches.Alpha.Domain.exception.DomainException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

/**
 * Before this existed, every domain rule violation and every bad enum value
 * (e.g. Places.valueOf on an unrecognized place) fell through to the default
 * Spring error page: a bare 500 with no message, indistinguishable from a
 * real bug. Map the known cases to the 4xx the controllers already document.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            MaxActiveParchesException.class,
            ParcheFullException.class,
            ActionNotAllowedException.class,
            DuplicateInvitationException.class,
            InvalidStateTransitionException.class,
            DomainException.class,
    })
    public ResponseEntity<Map<String, Object>> handleDomainException(RuntimeException ex) {
        return body(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /**
     * Covers Places/ParcheCategory/ParcheType.valueOf() on a value the client
     * sent that doesn't match any enum constant.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return body(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleUnexpected(Exception ex) {
        log.error("Unhandled exception", ex);
        return body(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error.");
    }

    private ResponseEntity<Map<String, Object>> body(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of(
                "message", message == null ? status.getReasonPhrase() : message,
                "status", status.value(),
                "timestamp", Instant.now().toString()));
    }
}
