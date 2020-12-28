package study.git2consul.properties;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties("zalopay.git2consul")
public class Git2ConsulProperties {

    private String dataKey = "stable";
    private String gitConfigFileName = "application.yaml";
    private List<ServiceConfig> list = new ArrayList<>();

    public ServiceConfig findServiceConfigByServiceName(String serviceName) {
        for (ServiceConfig config : this.list) {
            if (StringUtils.equals(serviceName, config.getServiceName())) {
                return config;
            }
        }
        return null;
    }

    @Getter
    @Setter
    public static class ServiceConfig {
        private String serviceName;
        private String pubPathConsul;
        private String secretPathConsul;
        private String pubPathGit;
        private String secretPathGit;
    }
}
