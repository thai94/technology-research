package study.git2consul.validate;

import org.apache.commons.lang3.StringUtils;
import study.git2consul.Git2ConsulProperties;
import study.git2consul.exception.InvalidParamsException;

public class InputValidate {

    public static void validateHost(String host) {
        if (StringUtils.isEmpty(host)) {
            throw new InvalidParamsException("host", host, "not empty");
        }
    }

    public static void validatePort(String port) {
        if (StringUtils.isEmpty(port)) {
            throw new InvalidParamsException("port", port, "not empty");
        }

        try {
            Integer.parseInt(port);
        } catch (NumberFormatException ex) {
            throw new InvalidParamsException("port", port, "not an integer");
        }
    }

    public static void validateToken(String token) {
        if (StringUtils.isEmpty(token)) {
            throw new InvalidParamsException("token", token, "not empty");
        }
    }

    public static void validateYamlFile(String yamlFile) {
        if (StringUtils.isEmpty(yamlFile)) {
            throw new InvalidParamsException("yamlFile", yamlFile, "not empty");
        }
    }

    public static void validateConsulPath(String consulPath) {
        if (StringUtils.isEmpty(consulPath)) {
            throw new InvalidParamsException("consulPath", consulPath, "not empty");
        }
    }

    public static void validateParams(Git2ConsulProperties properties) {
        validateHost(properties.getHost());
        validatePort(properties.getPort());
        validateToken(properties.getToken());
        validateYamlFile(properties.getYamlFile());
        validateConsulPath(properties.getConsulPath());
    }
}
