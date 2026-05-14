package com.smartcart.shared.infrastructure.persistence.jpa.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Enables JPA auditing for createdAt/updatedAt fields.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfiguration {
}

