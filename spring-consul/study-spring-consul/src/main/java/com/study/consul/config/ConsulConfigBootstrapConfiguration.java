/*
 * Copyright 2013-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.study.consul.config;

import com.ecwid.consul.v1.ConsulClient;
import com.study.consul.properties.ConsulConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.consul.ConsulAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Spencer Gibb
 * @author Edvin Eriksson
 */
@Configuration
@EnableConfigurationProperties(ConsulConfigProperties.class)
@Import(ConsulAutoConfiguration.class)
public class ConsulConfigBootstrapConfiguration {

	private ConsulConfigProperties configProperties;
	@Autowired
	private ConsulClient consul;

	public ConsulConfigBootstrapConfiguration(ConsulConfigProperties configProperties) {
		this.configProperties = configProperties;
	}

	@Bean
	public CustomPropertySourceLocator consulPropertySourceLocator(ConsulConfigProperties consulConfigProperties) throws IOException {

		Resource resource = new ClassPathResource("git.properties");
		Properties props = new Properties();
		PropertiesLoaderUtils.fillProperties(props, new EncodedResource(resource, "UTF-8"));
		return new CustomPropertySourceLocator(this.consul, consulConfigProperties, props.getProperty("git.build.version"));
	}
}
