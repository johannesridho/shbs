package com.shbs.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayConfiguration {

    private final DataSource dataSource;
    private final boolean autoClean;

    public FlywayConfiguration(DataSource dataSource, @Value("${flyway.auto-clean:false}") boolean autoClean) {
        this.dataSource = dataSource;
        this.autoClean = autoClean;
    }

    @Bean
    public Flyway flyway() {
        final Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        if (autoClean) {
            flyway.clean();
        }
        flyway.migrate();
        return flyway;
    }
}
