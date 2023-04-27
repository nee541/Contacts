package me.app.contacts.dao;

import java.util.List;
import me.app.contacts.model.Contact;

/**
 * This interface represents a contract for a DAO for the {@link Contact} model.
 * Note that all methods which returns the {@link Contact} from the DB, will not
 * fill the model with the password, due to security reasons.
 */
public interface ContactDAO {
    // Actions ------------------------------------------------------------------------------------

    /**
     * Returns the contact from the database matching the given ID, otherwise null.
     * @param id The ID of the user to be returned.
     * @return The user from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public Contact find(Long id) throws DAOException;
    /**
     * Returns a list of all users from the database ordered by user ID. The list is never null and
     * is empty when the database does not contain any user.
     * @return A list of all users from the database ordered by user ID.
     * @throws DAOException If something fails at database level.
     */
    public List<Contact> list() throws DAOException;

    /**
     * Create the given user in the database. The user ID must be null, otherwise it will throw
     * IllegalArgumentException. After creating, the DAO will set the obtained ID in the given user.
     * @param contact The contact to be created in the database.
     * @throws IllegalArgumentException If the user ID is not null.
     * @throws DAOException If something fails at database level.
     */
    public void create(Contact contact) throws IllegalArgumentException, DAOException;

    /**
     * Update the given user in the database. The user ID must not be null, otherwise it will throw
     * IllegalArgumentException. Note: the password will NOT be updated. Use changePassword() instead.
     * @param contact The contact to be updated in the database.
     * @throws IllegalArgumentException If the user ID is null.
     * @throws DAOException If something fails at database level.
     */
    public void update(Contact contact) throws IllegalArgumentException, DAOException;

    /**
     * Delete the given contact from the database. After deleting, the DAO will set the ID of the given
     * user to null.
     * @param contact The contact to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(Contact contact) throws DAOException;

    public void delete(Long id) throws DAOException;
}
