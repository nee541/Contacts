import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.ServletContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import me.app.contacts.dao.ContactDAO;
import me.app.contacts.dao.DAOException;
import me.app.contacts.dao.DAOFactory;
import me.app.contacts.model.Contact;

@WebServlet(name = "contactServlet", value = "/contact")
public class ContactServlet extends HttpServlet {
    public static final String CONTACT_ID = "id";
    public static final String CLIENT_ACTION = "action";
    public static final String ADD_NEW_CONTACT = "add";
    public static final String DELETE_CONTACT = "delete";
    public static final String UPDATE_CONTACT = "update";
    public static final String CONTACT_SORT_BY = "sort";
    public static final String GET_CONTACT_TYPE = "type";
    public static final String SEARCH_TERM = "q";

    private DAOFactory daoFactory;
    private ContactDAO contactDAO;
    private List<Contact> contactList;

    public void init() {
        daoFactory = DAOFactory.getInstance("contacts.jdbc");
        contactDAO = daoFactory.getContactDAO();
        contactList = contactDAO.list();
    }

    private List<Contact> searchContacts(List<Contact> unFilteredContacts, String term) {
        List<Contact> filteredContacts = new ArrayList<>();
        for (Contact item : unFilteredContacts) {
            if (item.getName().toUpperCase().contains(term.toUpperCase())) {
                filteredContacts.add(item);
            }
        }

        return filteredContacts;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ServletContext context=getServletContext();
        String searchTerm = request.getParameter(SEARCH_TERM);
        List<Contact> filteredContacts;
        if (searchTerm != null && !searchTerm.isEmpty()) {
            filteredContacts = searchContacts(contactList, searchTerm);
        }
        else {
            filteredContacts = contactList;
        }
        String strSortKey = request.getParameter(CONTACT_SORT_BY);
        if (strSortKey == null || strSortKey.isEmpty()) {
            context.setAttribute("contacts", filteredContacts);
        }
        else {
            int sortKey = Integer.parseInt(strSortKey);
            switch (sortKey) {
                // sort by add time (id), also the default
                case 1:
                    break;
                // sort by name
                case 2:
                    Collections.sort(contactList, new Comparator<Contact>() {
                        @Override
                        public int compare(Contact o1, Contact o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });
                    break;
                // sort by phone number
                case 3:
                    Collections.sort(contactList, new Comparator<Contact>() {
                        @Override
                        public int compare(Contact o1, Contact o2) {
                            long n1 = o1.getPhoneNumber().getNumber(),
                                    n2 = o2.getPhoneNumber().getNumber();
                            if (n1 < n2) {
                                return -1;
                            }
                            else if (n1 > n2) {
                                return 1;
                            }
                            else {
                                return 0;
                            }
                        }
                    });
                    break;
                default:
                    break;
            }
        }
        context.setAttribute("contactNum", filteredContacts.isEmpty() ? 0 : filteredContacts.size());

        String contactId = request.getParameter(CONTACT_ID);
        String type = request.getParameter(GET_CONTACT_TYPE);

        if (type == null || type.isEmpty()) {
            if (contactId != null) {
                Long specifiedId = Long.parseLong(contactId);
                context.setAttribute(CONTACT_ID, specifiedId);
                Contact specifiedContact = contactDAO.find(specifiedId);
                if (specifiedContact == null) {
                    context.setAttribute("redirectMsg", "This contact have been deleted");
                    context.getRequestDispatcher("/WEB-INF/redirect.jsp").forward(request, response);
                }
                else {
                    context.setAttribute("name", specifiedContact.getName());
                    context.setAttribute("phoneNumber", specifiedContact.getPhoneNumber());
                    context.setAttribute("extraInfo", specifiedContact.getExtraInfoMap());
                    context.getRequestDispatcher("/WEB-INF/overview.jsp").forward(request, response);
                }
            }
            else {
                context.getRequestDispatcher("/WEB-INF/overview.jsp").forward(request, response);
            }
        }
        else if (contactId != null){
            if (type.equalsIgnoreCase("json")) {
                System.out.println("API");
                Long specifiedId = Long.parseLong(contactId);
                context.setAttribute(CONTACT_ID, specifiedId);
                Contact specifiedContact = contactDAO.find(specifiedId);
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                out.print(specifiedContact.toJsonString());
                out.flush();
                System.out.println(specifiedContact.toJsonString());
            }
        }
    }

    private Contact mapContactFromRequest(HttpServletRequest request) throws IOException {
        BufferedReader reader = request.getReader();
        String postJson = reader.lines().collect(Collectors.joining());
        System.out.println(postJson);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(postJson, Contact.class);
    }

    private void addContact(HttpServletRequest request, HttpServletResponse response) throws IOException, DAOException {
        Contact newContact = mapContactFromRequest(request);
        System.out.println(newContact.toString());

        try {
            contactDAO.create(newContact);
        } catch (DAOException e) {
            System.out.println(e.getMessage());
        }

        contactList.add(newContact);

        response.setStatus(200);
    }

    private void deleteContact(HttpServletRequest request, HttpServletResponse response) throws IOException, DAOException {
        String stringId = request.getParameter(CONTACT_ID);
        if (stringId == null || stringId.isEmpty()) {
            // error
        }
        Long id = Long.parseLong(stringId);
        try{
            System.out.println("Start deleting");
            contactDAO.delete(id);
            System.out.println("Finish deleting");
            contactList = contactDAO.list();
        } catch (DAOException daoException) {
            // error
        }
    }

    private void updateContact(HttpServletRequest request, HttpServletResponse response) throws IOException, DAOException {
        String stringId = request.getParameter(CONTACT_ID);
        if (stringId == null || stringId.isEmpty()) {
            // error
        }
        Long id = Long.parseLong(stringId);
        Contact updatedContact = mapContactFromRequest(request);
        updatedContact.setId(id);

        contactDAO.update(updatedContact);
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("DO PUT");
        String target = request.getParameter(CLIENT_ACTION).toLowerCase();
        System.out.println(target);
        switch (target) {
            case ADD_NEW_CONTACT:
                addContact(request, response);
                break;
            case DELETE_CONTACT:
                deleteContact(request, response);
                break;
            case UPDATE_CONTACT:
                updateContact(request, response);
                break;
            default:
                break;
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("DO POST");
        doPut(request, response);
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        deleteContact(request, response);
    }

    public void destroy() {
    }
}
