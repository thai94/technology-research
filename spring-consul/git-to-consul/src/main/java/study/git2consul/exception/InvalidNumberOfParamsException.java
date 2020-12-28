package study.git2consul.exception;

public class InvalidNumberOfParamsException extends ZalopayRuntimeException {

    public InvalidNumberOfParamsException(String parms) {
        super(String.format("Invalid number of params. Actual: %s, Expected: args[0] serviceName, args[1] releaseVersion, args[2] env, args[3] workSpaceDir, args[4] syncType", parms));
    }

}