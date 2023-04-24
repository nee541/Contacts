package me.app.contacts.dao;

import static me.app.contacts.dao.DAOUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import me.app.contacts.model.Contact;
import me.app.contacts.model.PhoneNumber;

/**
 * This class represents a concrete JDBC implementation of the {@link ContactDAO} interface.
 */
public class ContactDAOJDBC implements ContactDAO {
    // Constants ----------------------------------------------------------------------------------
    public static final String SQL_FIND_BY_ID =
            "select id, name, phonenumber, extrainfo from Contact where id = ?";
    public static final String SQL_LIST_ORDER_BY_NAME =
            "select id, name, phonenumber, extrainfo from Contact order by name";
    public static final String SQL_LIST_ORDER_BY_ID =
            "select id, name, phonenumber, extrainfo from Contact order by id";
    public static final String SQL_INSERT =
            "insert into Contact(name, phonenumber, extrainfo) values (?, ?, ?)";
    public static final String SQL_UPDATE =
            "update Contact set name = ?, phonenumber = ?, extrainfo = ? where id = ?";
    public static final String SQL_DELETE =
            "delete from Contact where id = ?";

    // Vars ---------------------------------------------------------------------------------------
    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct an Contact DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this User DAO for.
     */
    ContactDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    // Actions ------------------------------------------------------------------------------------
    public Contact find(Long id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    /**
     * Returns the contact from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The user from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private Contact find(String sql, Object... values) throws DAOException {
        Contact contact = null;

        try (
                Connection connection = daoFactory.getConnection();
                PreparedStatement statement = prepareStatement(connection, sql, false, values);
                ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                contact = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return contact;
    }

    /**
     * Map the current row of the given ResultSet to a Contact.
     * @param resultSet The ResultSet of which the current row is to be mapped to a Contact.
     * @return The mapped User from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    private static Contact map(ResultSet resultSet) throws SQLException {
        Contact contact = new Contact();
        contact.setId(resultSet.getLong(1));
        contact.setName(resultSet.getString(2));
        contact.setPhoneNumber(new PhoneNumber(resultSet.getLong(3)));
        contact.setExtraInfoMap(resultSet.getString(4));

        return contact;
    }

    @Override
    public List<Contact> list() throws DAOException {
        List<Contact> contacts = new ArrayList<>();

        try(
                Connection connection = daoFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
                ResultSet resultSet = statement.executeQuery();
                ) {
            while(resultSet.next()) {
                contacts.add(map(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return contacts;
    }

    @Override
    public void create(Contact contact) throws IllegalArgumentException, DAOException {
        if (contact.getId() != null) {
            throw new IllegalArgumentException("Contact is already created, this contact ID is not null");
        }

        Object[] values = { contact.getName(), contact.getPhoneNumber().getNumber(), contact.getExtraInfoJsonString() };

        try (
                Connection connection = daoFactory.getConnection();
                PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);
                ) {
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Creating contact failed, no rows affected.");
            }

            try (ResultSet generateKeys = statement.getGeneratedKeys()) {
                if (generateKeys.next()) {
                    contact.setId(generateKeys.getLong(1));
                } else {
                    throw new DAOException("Creating user failed, no generated key obtained.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public void update(Contact contact) throws IllegalArgumentException, DAOException {
        if (contact.getId() == null) {
            throw new IllegalArgumentException("Contact is not created yet, the contact ID is null.");
        }
        Object[] values = { contact.getName(), contact.getPhoneNumber().getNumber(), contact.getExtraInfoJsonString(), contact.getId().longValue() };

        try (
                Connection connection = daoFactory.getConnection();
                PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
                ) {
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Updating contact failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(Contact contact) throws DAOException {
        Object[] values = { contact.getId().longValue() };

        try (
                Connection connection = daoFactory.getConnection();
                PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
                ) {
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Deleting contact failed, no rows affected.");
            } else {
                contact.setId(null);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }
}
