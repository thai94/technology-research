package study.git2consul.exception;

public class ServiceNotFoundException extends ZalopayRuntimeException {

    public ServiceNotFoundException(String serviceName) {
        super(String.format("The serviceName: %s not found. Please check in your application.yaml file.", serviceName));
    }

}