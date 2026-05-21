package Excepciones;

public class UnknownPaymentException extends RuntimeException {
    public UnknownPaymentException() {
        super("No se ha encontrado el pago💵⚠️");;
    }
}
