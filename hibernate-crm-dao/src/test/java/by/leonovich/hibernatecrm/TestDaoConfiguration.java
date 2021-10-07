package by.leonovich.hibernatecrm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.jdbcx.JdbcDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;


/**
 * Created : 04/04/2021 12:03
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@Configuration
@Import(DaoConfiguration.class)
public class TestDaoConfiguration {
    protected static final Logger log = LogManager.getLogger(TestDaoConfiguration.class);

    @Value("${test_database_driver_classname}")
    private String testDriverClassName;
    @Value("${test_database_url}")
    private String testUrl;
    @Value("${test_database_username}")
    private String testUsername;
    @Value("${test_database_password}")
    private String testPassword;

    @Bean
    public DataSource dataSource() {
        log.info("DRIVER : {}; URL : {}; USER : {}; PASSWORD : {}", testDriverClassName, testUrl, testUsername, testPassword);
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl(testUrl);
        dataSource.setUser(testUsername);
        dataSource.setPassword(testPassword);
        return dataSource;
    }
}
