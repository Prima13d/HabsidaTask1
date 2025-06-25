package jm.task.core.jdbc.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import jm.task.core.jdbc.model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    private static final String URL = "jdbc:mysql://localhost:3306/example_schema";
    private static final String USER = "root";
    private static final String PASSWORD = "25626034be";
    public static SessionFactory sessionFactory;

    private Util() {
        throw new UnsupportedOperationException("Util class should not be instantiated");
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Configuration config = new Configuration();
            config.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
            config.setProperty("hibernate.connection.url", URL);
            config.setProperty("hibernate.connection.username", USER);
            config.setProperty("hibernate.connection.password", PASSWORD);
            config.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
            config.setProperty("hibernate.show_sql", "true");
            config.setProperty("hibernate.current_session_context_class", "thread");

            config.addAnnotatedClass(User.class);

            sessionFactory = config.buildSessionFactory(
                    new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build()
            );
        }
        return sessionFactory;
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Connection failed");
            throw new RuntimeException(e);
        }
    }
}
