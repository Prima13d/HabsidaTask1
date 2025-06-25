package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;





public class UserDaoJDBCImpl implements UserDao {
    private static final Logger logger = Logger.getLogger(UserDaoJDBCImpl.class.getName());

    @Override
    public void createUsersTable() {
        try (Connection conn = Util.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS users (
                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                    name VARCHAR(50),
                    lastName VARCHAR(50),
                    age TINYINT
                )
            """);
            logger.info("Таблица 'users' успешно создана (если не существовала).");
        } catch (SQLException e) {
            logger.severe("Ошибка при создании таблицы 'users': " + e.getMessage());
        }
    }

    @Override
    public void dropUsersTable() {
        try (Connection conn = Util.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DROP TABLE IF EXISTS users");
            logger.info("Таблица 'users' успешно удалена (если существовала).");
        } catch (SQLException e) {
            logger.severe("Ошибка при удалении таблицы 'users': " + e.getMessage());
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Connection conn = Util.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO users (name, lastName, age) VALUES (?, ?, ?)")) {

            ps.setString(1, name);
            ps.setString(2, lastName);
            ps.setByte(3, age);

            ps.executeUpdate();
            logger.info(String.format("Пользователь %s %s успешно добавлен в базу данных.", name, lastName));
        } catch (SQLException e) {
            logger.severe("Ошибка при добавлении пользователя: " + e.getMessage());
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Connection conn = Util.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM users WHERE id = ?")) {
            ps.setLong(1, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Пользователь с ID " + id + " удалён из базы данных.");
            } else {
                logger.info("Пользователь с ID " + id + " не найден.");
            }
        } catch (SQLException e) {
            logger.severe("Ошибка при удалении пользователя по ID: " + e.getMessage());
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection conn = Util.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM users")) {

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));
                user.setLastName(rs.getString("lastName"));
                user.setAge(rs.getByte("age"));
                users.add(user);
            }

            logger.info("Получено " + users.size() + " пользователей из базы данных.");

        } catch (SQLException e) {
            logger.severe("Ошибка при получении списка пользователей: " + e.getMessage());
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        try (Connection conn = Util.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("TRUNCATE TABLE users");
            logger.info("Таблица 'users' успешно очищена.");
        } catch (SQLException e) {
            logger.severe("Ошибка при очистке таблицы 'users': " + e.getMessage());
        }
    }
}
