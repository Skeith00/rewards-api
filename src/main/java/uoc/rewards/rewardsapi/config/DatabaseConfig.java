package uoc.rewards.rewardsapi.config;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import uoc.rewards.rewardsapi.config.properties.DBProperties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
        basePackages = DatabaseConfig.IDENTITY_JPA_REPOSITORY_PACKAGE,
        entityManagerFactoryRef = "identityEntityManager",
        transactionManagerRef = DatabaseConfig.TRANSACTION_MANAGER
)
public class DatabaseConfig {
    public static final String TRANSACTION_MANAGER = "identityTransactionManager";
    public static final String IDENTITY_JPA_ENTITY_PACKAGE = "uoc.rewards.rewardsapi.model";
    public static final String IDENTITY_JPA_REPOSITORY_PACKAGE = "uoc.rewards.rewardsapi.repository";

    @Value("${application.resource.database.url}")
    private String url;
    @Value("${application.resource.database.username}")
    private String username;
    @Value("${application.resource.database.password}")
    private String password;

    @Bean
    public DataSource dataSource(DBProperties dbProperties) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dbProperties.getUrl());
        config.setUsername(dbProperties.getUsername());
        config.setPassword(dbProperties.getPassword());
        return new HikariDataSource(config);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean identityEntityManager(EntityManagerFactoryBuilder builder, DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factory = builder.dataSource(dataSource).build();

        factory.setDataSource(dataSource);
        factory.setPackagesToScan(IDENTITY_JPA_ENTITY_PACKAGE);
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        factory.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        factory.setJpaPropertyMap(properties);

        return factory;
    }

    @Bean
    public PlatformTransactionManager identityTransactionManager(EntityManagerFactory entityManager) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManager);

        return transactionManager;
    }
}
