package com.study.consul.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Spencer Gibb
 */
@Validated
@ConfigurationProperties("zalopay.starter.consul.config")
public class ConsulConfigProperties {
    private String prefix = "config";
    @NotEmpty
    private String defaultContext = "application";
    @NotEmpty
    private String profileSeparator = "-";
    @NotNull
    private Format format = Format.YAML;
    @NotEmpty
    private String dataKey = "data";
    @Value("${consul.token:${CONSUL_TOKEN:${spring.cloud.consul.token:${SPRING_CLOUD_CONSUL_TOKEN:}}}}")
    private String aclToken;
    private String name;

    public enum Format {

        /**
         * Indicates that the configuration specified in consul is of property style i.e.,
         * value of the consul key would be a list of key=value pairs separated by new
         * lines.
         */
        PROPERTIES,

        /**
         * Indicates that the configuration specified in consul is of YAML style i.e.,
         * value of the consul key would be YAML format.
         */
        YAML
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getDefaultContext() {
        return defaultContext;
    }

    public void setDefaultContext(String defaultContext) {
        this.defaultContext = defaultContext;
    }

    public String getProfileSeparator() {
        return profileSeparator;
    }

    public void setProfileSeparator(String profileSeparator) {
        this.profileSeparator = profileSeparator;
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public String getDataKey() {
        return dataKey;
    }

    public void setDataKey(String dataKey) {
        this.dataKey = dataKey;
    }

    public String getAclToken() {
        return aclToken;
    }

    public void setAclToken(String aclToken) {
        this.aclToken = aclToken;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
