import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import me.app.contacts.dao.DAOFactory;
import me.app.contacts.dao.ContactDAO;
import me.app.contacts.model.Contact;
import me.app.contacts.model.PhoneNumber;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test harness for the com.example.dao package. This require the following preconditions:
 * <ol>
 * <li>A MySQL server running at localhost:3306 with a database named 'contacts'.
 * <li>A 'Contact' table in the 'contacts' database which is created as follows:
 * <pre>
 * create table Contact (
 * 	id bigint unsigned not null auto_increment,
 *     name text character set utf8mb4 collate utf8mb4_bin,
 *     phonenumber bigint unsigned not null,
 *     extrainfo json,
 *
 *     primary key (id)
 * );
 * </pre>
 * <li>A MySQL user with the name 'contactsApp' and password '242017' which has sufficient rights on
 * the contacts.Contact table.
 * <li>A MySQL JDBC Driver JAR file in the classpath.
 * <li>A dao.properties file 'dao.properties' in the classpath with the following entries:
 * <pre>
 * contacts.jdbc.driver = com.mysql.jdbc.Driver
 * contacts.jdbc.url = jdbc:mysql://localhost:3306/contacts
 * contacts.jdbc.username = contactsApp
 * contacts.jdbc.password = 242017
 * </pre>
 * </ol>
 */
public class DAOTest {

    public static void main(String[] args) throws Exception {
        String json = "{\"phoneNumber\": \"111\", \"name\": \"example\", \"extraInfo\": {\"first\": \"first value\", \"second\": \"second value\"}}";
        ObjectMapper mapper = new ObjectMapper();
        Contact newContact = mapper.readValue(json, Contact.class);
        System.out.println(newContact.toString());
        System.out.println(newContact.toJsonString());

        // Obtain DAOFactory.
        DAOFactory contactsDB = DAOFactory.getInstance("contacts.jdbc");
        System.out.println("DAOFactory successfully obtained: " + contactsDB);

        // Obtain ContactDAO.
        ContactDAO contactDAO = contactsDB.getContactDAO();
        System.out.println("ContactDAO successfully obtained: " + contactDAO);

        // Create user.
        Contact contact = new Contact("test1", new PhoneNumber(19801298267L), new HashMap<String, String>());
        contactDAO.create(contact);
        System.out.println("User successfully created: " + contact);

        // Create another user.
        Map<String, String> anotherMap = new HashMap<String, String>();
        anotherMap.put("1", "2");
        Contact anotherContact = new Contact("test2", new PhoneNumber(324), anotherMap);
        contactDAO.create(anotherContact);
        System.out.println("Another user successfully created: " + anotherContact);

        // Update user.
        contact.setName("foo");
        contactDAO.update(contact);
        System.out.println("User successfully updated: " + contact);

        // List all users.
        List<Contact> contacts = contactDAO.list();
        System.out.println("List of users successfully queried: " + contacts);
        System.out.println("Thus, amount of users in database is: " + contacts.size());

        // Delete user.
        contactDAO.delete(contact);
        System.out.println("User successfully deleted: " + contact);

        // List all users.
        contacts = contactDAO.list();
        System.out.println("List of users successfully queried: " + contacts);
        System.out.println("Thus, amount of users in database is: " + contacts.size());
    }
}