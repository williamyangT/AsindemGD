package com.asindem.gestiondocumental.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://195.170.0.1:3306/asindem_gestion_documental_v2");
        dataSource.setUsername("william");
        dataSource.setPassword("Asindem_49");
        return dataSource;
    }
}
