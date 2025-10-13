package com.nove.sule.backend_nove_sule.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Configuración JPA para habilitar auditoría automática
 */
@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.nove.sule.backend_nove_sule.repository")
public class JpaConfig {
}
