package study.git2consul.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("zalopay.git2consul")
public class Git2ConsulProperties {
    private String contextPub;
    private String contextSecret;

    public String getContextPub() {
        return contextPub;
    }

    public void setContextPub(String contextPub) {
        this.contextPub = contextPub;
    }

    public String getContextSecret() {
        return contextSecret;
    }

    public void setContextSecret(String contextSecret) {
        this.contextSecret = contextSecret;
    }
}
