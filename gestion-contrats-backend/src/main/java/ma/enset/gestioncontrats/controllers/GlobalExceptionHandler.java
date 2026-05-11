package ma.enset.gestioncontrats.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Gestionnaire global d'exceptions.
 *
 * @RestControllerAdvice : intercepte toutes les exceptions levées dans
 * n'importe quel Controller et retourne une réponse JSON propre.
 * Plus besoin de try/catch dans chaque controller.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Gère les erreurs de validation (@Valid) : retourne les champs invalides
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("status", 400);
        response.put("erreurs", errors);

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Gère les RuntimeException lancées depuis les services (entité introuvable, règle métier...)
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("status", 400);
        response.put("message", ex.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Filet de sécurité : toute autre exception non prévue
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("status", 500);
        response.put("message", "Erreur interne du serveur");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
