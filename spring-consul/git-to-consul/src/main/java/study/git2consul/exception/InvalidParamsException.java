package study.git2consul.exception;

public class InvalidParamsException extends ZalopayRuntimeException {

    public InvalidParamsException(String paramName, String actualValue, String expectedValue) {
        super(String.format("Invalid params: %s. Actual: %s, Expected: %s", paramName, actualValue, expectedValue));
    }

}