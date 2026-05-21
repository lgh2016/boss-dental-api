package mx.com.bossdental.api.exceptions;

/**
 * Excepción de reglas de negocio.
 */
public class BusinessException extends RuntimeException {

    /**
     * Constructor.
     *
     * @param message mensaje de error.
     */
    public BusinessException(String message) {
        super(message);
    }

}