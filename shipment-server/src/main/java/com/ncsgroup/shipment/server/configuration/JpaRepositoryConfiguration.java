package com.ncsgroup.shipment.server.configuration;

import com.ncsgroup.shipment.server.repository.BaseRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.ncsgroup.shipment.server",
        repositoryBaseClass = BaseRepository.class
)
public class JpaRepositoryConfiguration {
}
