package by.leonovich.hibernatecrm;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

/**
 * Created : 05/04/2021 17:58
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@Configuration
@ComponentScan("by.leonovich.hibernatecrm")
@EnableAspectJAutoProxy
@Import(DaoConfiguration.class)
public class ServiceConfiguration {
}
