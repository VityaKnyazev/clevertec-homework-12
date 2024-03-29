package ru.clevertec.ecl.knyazev.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    private static final String[] ENTITY_PACKAGES = {"ru.clevertec.ecl.knyazev.entity"};

    @Bean
    DataSource hikariDataSource(DataSourceProperties dataSourceProperties) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(dataSourceProperties.driverClassName());
        hikariConfig.setJdbcUrl(dataSourceProperties.jdbcUrl());
        hikariConfig.setUsername(dataSourceProperties.username());
        hikariConfig.setPassword(dataSourceProperties.password());
        hikariConfig.setMaximumPoolSize(dataSourceProperties.maxPoolSize());
        hikariConfig.setConnectionTimeout(dataSourceProperties.connectionTimeout());

        return new HikariDataSource(hikariConfig);
    }

    @Bean
    JdbcTemplate jdbcTemplate(DataSource hikariDataSource) {
        return new JdbcTemplate(hikariDataSource);
    }

    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource hikariDataSource,
                                                                Map<String, ?> hibernateProperties) {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(hikariDataSource);
        entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactory.setPackagesToScan(ENTITY_PACKAGES);
        entityManagerFactory.setJpaPropertyMap(hibernateProperties);
        return entityManagerFactory;
    }

    @Bean
    JpaTransactionManager JpaTransactionManager(LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
        return new JpaTransactionManager(entityManagerFactoryBean.getObject());
    }
}
