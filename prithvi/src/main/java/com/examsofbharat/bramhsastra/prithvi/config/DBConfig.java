package com.examsofbharat.bramhsastra.prithvi.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactory",
        basePackages = {"com.examsofbharat.bramhsastra.prithvi.dao"}
)
public class DBConfig {

    @Value("${spring.datasource.url}")
    private String SPRING_DATASOURCE_URL;
    @Value("${spring.datasource.username}")
    private String SPRING_DATASOURCE_USERNAME;
    @Value("${spring.datasource.password}")
    private String SPRING_DATASOURCE_PASSWORD;
    @Value("${spring.datasource.db.driver-class-name}")
    private String SPRING_DATASOURCE_DB_DRIVER_CLASS_NAME;



    @Primary
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(SPRING_DATASOURCE_DB_DRIVER_CLASS_NAME);
        dataSource.setUrl(SPRING_DATASOURCE_URL);
        dataSource.setUsername(SPRING_DATASOURCE_USERNAME);
        dataSource.setPassword(SPRING_DATASOURCE_PASSWORD);
        return dataSource;
    }

    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean
    entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("dataSource") DataSource dataSource
    ) {
        return builder
                .dataSource(dataSource)
                .packages("com.examsofbharat.bramhsastra.prithvi.entity")
                //.persistenceUnit("foo")
                .build();
    }

    @Primary
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("entityManagerFactory") EntityManagerFactory
                    entityManagerFactory
    ) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
