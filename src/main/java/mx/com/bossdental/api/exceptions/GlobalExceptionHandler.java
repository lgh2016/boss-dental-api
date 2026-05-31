package mx.com.bossdental.api.exceptions;

import lombok.extern.slf4j.Slf4j;
import mx.com.bossdental.api.common.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manejo global de excepciones de la aplicación.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja errores de negocio.
     *
     * @param ex excepción de negocio
     * @return respuesta de error controlada
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<Void> handleBusinessException(
            BusinessException ex) {

        return Response.<Void>builder()
                .success(Boolean.FALSE)
                .message(ex.getMessage())
                .build();
    }

    /**
     * Maneja errores internos no controlados.
     *
     * @param ex excepción inesperada
     * @return respuesta de error genérica
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<Void> handleException(
            Exception ex) {

        log.error("Error interno no controlado", ex);

        return Response.<Void>builder()
                .success(Boolean.FALSE)
                .message("Ocurrió un error interno")
                .build();
    }
}