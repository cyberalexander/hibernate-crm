package by.leonovich.hibernatecrm.hibernate;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

/**
 * Hibernate 5 provides two different naming strategies for use with Hibernate entities:
 * 1) an Implicit Naming Strategy
 * 2) and a Physical Naming Strategy.
 * <p>
 * The Implicit Naming Strategy governs how Hibernate derives a logical name from our Java class and property names.
 * We can select from four built-in strategies, or we can create our own.
 * <p>
 * Hibernate uses the Physical Naming Strategy to map our logical names to a SQL table and its columns.
 * <p>
 * Created : 05/12/2020 16:25
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public class CustomPhysicalNamingStrategy extends PhysicalNamingStrategyStandardImpl {

    @Override
    public Identifier toPhysicalCatalogName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return super.toPhysicalCatalogName(identifier, jdbcEnvironment);
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return super.toPhysicalSchemaName(identifier, jdbcEnvironment);
    }

    @Override
    public Identifier toPhysicalTableName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return super.toPhysicalTableName(identifier, jdbcEnvironment);
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return super.toPhysicalSequenceName(identifier, jdbcEnvironment);
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return super.toPhysicalColumnName(identifier, jdbcEnvironment);
    }
}
