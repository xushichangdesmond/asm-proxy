package powerdancer.asmproxy;

public class AsmProxyException extends RuntimeException {

    public AsmProxyException() {
    }

    public AsmProxyException(String message) {
        super(message);
    }

    public AsmProxyException(String message, Throwable cause) {
        super(message, cause);
    }

    public AsmProxyException(Throwable cause) {
        super(cause);
    }

    public AsmProxyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
