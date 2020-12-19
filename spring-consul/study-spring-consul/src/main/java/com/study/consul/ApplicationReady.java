package com.study.consul;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.bootstrap.config.BootstrapPropertySource;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

@Component
public class ApplicationReady {

    private static final Logger logger = LogManager.getLogger();

    @Autowired
    private ConfigurableEnvironment configurableEnvironment;

    @EventListener(ApplicationReadyEvent.class)
    public void ready() {
        logger.info("##################### File Properties #####################");
        showConfigProperties();
        logger.info("##################### Application Started On Port {} #####################", 8080);
    }

    private void showConfigProperties() {
        configurableEnvironment.getPropertySources().forEach(it -> {
            if (it instanceof BootstrapPropertySource) {
                for (String key : ((BootstrapPropertySource<?>) it).getPropertyNames()) {
                    logger.info(key + "=" + it.getProperty(key));
                }
            }
        });
    }

}
