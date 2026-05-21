package Excepciones;

public class UnknownPaymentException extends RuntimeException {
    public UnknownPaymentException(String message) {
        super(message);
    }
}
