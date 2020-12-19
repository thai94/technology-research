package com.study.consul.config;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.Response;
import com.study.consul.properties.ConsulConfigProperties;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class CustomPropertySourceLocator implements PropertySourceLocator {

    protected static final List<String> DIR_SUFFIXES = Collections.singletonList("/");

    private final ConsulClient consul;
    private final ConsulConfigProperties properties;
    private final String version;

    public CustomPropertySourceLocator(ConsulClient consul, ConsulConfigProperties properties, String version) {
        this.consul = consul;
        this.properties = properties;
        this.version = version;
    }

    @Override
    public PropertySource<?> locate(Environment environment) {

        if (environment instanceof ConfigurableEnvironment) {
            ConfigurableEnvironment env = (ConfigurableEnvironment) environment;
            List<String> profiles = Arrays.asList(env.getActiveProfiles());
            List<String> contexts = getAutomaticContexts(profiles);

            CompositePropertySource composite = new CompositePropertySource("consul");
            for (String context : contexts) {
                ConsulPropertySource propertySource = new ConsulPropertySource(context, this.consul, this.properties);
                propertySource.init();
                composite.addPropertySource(propertySource);
            }
            return composite;
        }
        return null;
    }

    private List<String> getAutomaticContexts(List<String> profiles) {
        List<String> contexts = new ArrayList<>();
        String prefix = properties.getPrefix();
        String defaultContext = getContext(prefix, properties.getDefaultContext());
        List<String> suffixes = DIR_SUFFIXES;
        for (String suffix : suffixes) {
            addProfiles(contexts, defaultContext, profiles, suffix);
        }

        String baseContext = getContext(prefix, properties.getName());
        for (String suffix : suffixes) {
            addProfiles(contexts, baseContext, profiles, suffix);
        }

        List<String> filterContexts = new ArrayList<>();
        for (String context: contexts) {
            Response<List<String>> keysResp = this.consul.getKVKeysOnly(context, "",this.properties.getAclToken());
            List<String> keys = keysResp.getValue();
            Pattern pattern = Pattern.compile(context + version + "-[0-9]+/" + this.properties.getDataKey() + "$");
            Pattern patternNoVersion = Pattern.compile(context + this.properties.getDataKey() + "$");
            List<String> filterKeys = new ArrayList<>();
            for(String key : keys) {
                if(pattern.matcher(key).matches() || patternNoVersion.matcher(key).matches()) {
                    filterKeys.add(key);
                }
            }
            Collections.sort(filterKeys);
            filterContexts.add(filterKeys.get(filterKeys.size() - 1));
        }

        // we build them backwards, first wins, so reverse
//        Collections.reverse(contexts);

        return filterContexts;
    }

    private String getContext(String prefix, String context) {
        if (StringUtils.isEmpty(prefix)) {
            return context;
        }
        else {
            return prefix + "/" + context;
        }
    }

    private void addProfiles(List<String> contexts, String baseContext, List<String> profiles, String suffix) {
        for (String profile : profiles) {
            contexts.add(baseContext + properties.getProfileSeparator() + profile + suffix);
        }
    }
}
