package com.ivmarchenko.useraggregator.strategy;

import com.ivmarchenko.useraggregator.config.DatabaseConfig;
import com.ivmarchenko.useraggregator.config.DatabaseListConfig;
import com.ivmarchenko.useraggregator.strategy.naming.CustomPhysicalNamingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * Strategy interface for configuring database-specific components such as DataSource,
 * JPA properties, and EntityManagerFactory.
 */
public interface DatabaseStrategy {

    Logger logger = LoggerFactory.getLogger(DatabaseStrategy.class);

    // Hibernate property constants
    String HIBERNATE_DIALECT = "hibernate.dialect";
    String HIBERNATE_HBM2DDL_AUTO = "hibernate.hbm2ddl.auto";
    String HIBERNATE_SHOW_SQL = "hibernate.show_sql";
    String HIBERNATE_FORMAT_SQL = "hibernate.format_sql";

    /**
     * Creates a {@link DataSource} based on the provided {@link DatabaseConfig}.
     *
     * @param config the database configuration containing connection details
     * @return a configured {@link DataSource}
     */
    DataSource createDataSource(DatabaseConfig config);

    /**
     * Creates a {@link LocalContainerEntityManagerFactoryBean} configured for the database strategy.
     *
     * @param dataSource          the configured {@link DataSource}
     * @param persistenceUnitName the name of the persistence unit
     * @param mapping             a map of column mappings
     * @param tableName           the table name for mapping
     * @return a configured {@link LocalContainerEntityManagerFactoryBean}
     */
    default LocalContainerEntityManagerFactoryBean createEntityManagerFactory(
            DataSource dataSource,
            String persistenceUnitName,
            Map<String, String> mapping,
            String tableName
    ) {
        logger.info("Creating EntityManagerFactory for persistence unit: {}", persistenceUnitName);

        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("com.ivmarchenko.useraggregator.model");
        emf.setPersistenceUnitName(persistenceUnitName);

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        emf.setJpaVendorAdapter(vendorAdapter);

        Properties jpaProperties = getJpaProperties();
        configureNamingStrategy(jpaProperties, mapping, tableName);
        emf.setJpaProperties(jpaProperties);

        return emf;
    }

    /**
     * Configures the physical naming strategy using {@link CustomPhysicalNamingStrategy}.
     *
     * @param jpaProperties the JPA properties to configure
     * @param mapping       the column mappings
     * @param tableName     the table name mapping
     */
    default void configureNamingStrategy(Properties jpaProperties, Map<String, String> mapping, String tableName) {
        logger.debug("Configuring naming strategy with tableName: {} and mapping: {}", tableName, mapping);

        Map<String, String> tableMapping = new HashMap<>();
        tableMapping.put("users", Objects.nonNull(tableName) && !tableName.isBlank() ? tableName : "users");

        CustomPhysicalNamingStrategy namingStrategy = new CustomPhysicalNamingStrategy(tableMapping, mapping);
        jpaProperties.put("hibernate.physical_naming_strategy", namingStrategy);
    }

    /**
     * Provides JPA properties specific to the database strategy.
     *
     * @return a {@link Properties} instance with JPA properties
     */
    Properties getJpaProperties();
}
