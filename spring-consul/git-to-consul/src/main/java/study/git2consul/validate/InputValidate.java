package study.git2consul.validate;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import study.git2consul.exception.InvalidParamsException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidate {

    private static final String MSG_EXPLAIN_TEMPLATE = "Form Validate for field %s fail. Actual: %s, Expected: %s";
    private static final Pattern VERSION_PATTERN = Pattern.compile("^[0-9]+\\.[0-9]+\\.[0-9]+-[0-9]+$");
    private static final Pattern ENV_PATTERN = Pattern.compile("dev|qc|stg|staging|real|production");
    private static Logger LOG = LoggerFactory
            .getLogger(InputValidate.class);

    private static void validateServiceName(String serviceName) {
        if (StringUtils.isEmpty(serviceName)) {
            throw new InvalidParamsException("serviceName", serviceName, "not empty");
        }
    }

    private static void validateReleaseVersion(String releaseVersion) {
        Matcher matcher = VERSION_PATTERN.matcher(releaseVersion);
        if (!matcher.matches()) {
            throw new InvalidParamsException("releaseVersion", releaseVersion, "format: x.y.z-<incresing number>");
        }
    }

    private static void validateEnv(String env) {
        if (StringUtils.isEmpty(env)) {
            throw new InvalidParamsException("env", env, "not empty");
        }

        Matcher matcher = ENV_PATTERN.matcher(env);
        if (matcher.matches()) {
            throw new InvalidParamsException("env", env, "format: dev|qc|stg|staging|real|production");
        }
    }

    private static void validateWorkSpaceDir(String workSpaceDir) {
        if (StringUtils.isEmpty(workSpaceDir)) {
            throw new InvalidParamsException("workSpaceDir", workSpaceDir, "not empty");
        }
    }

    public static void validatePubParams(String serviceName, String releaseVersion, String env, String workSpaceDir) {
        validateServiceName(serviceName);
        validateReleaseVersion(releaseVersion);
        validateEnv(env);
        validateWorkSpaceDir(workSpaceDir);
    }

    public static void validateSecretParams(String serviceName, String env, String workSpaceDir) {

        validateServiceName(serviceName);
        validateEnv(env);
        validateWorkSpaceDir(workSpaceDir);
    }
}
