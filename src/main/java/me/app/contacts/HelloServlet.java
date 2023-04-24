package me.app.contacts;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.ServletContext;

import me.app.contacts.dao.ContactDAO;
import me.app.contacts.dao.DAOFactory;
import me.app.contacts.model.Contact;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    public static final String CONTACT_ID = "id";

    private DAOFactory daoFactory;
    private ContactDAO contactDAO;
    private List<Contact> contactList;

    public void init() {
        daoFactory = DAOFactory.getInstance("contacts.jdbc");
        contactDAO = daoFactory.getContactDAO();
        contactList = contactDAO.list();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ServletContext context=getServletContext();
        context.setAttribute("contacts", contactList);
        context.setAttribute("contactNum", contactList.isEmpty() ? 0 : contactList.size());

        String contactId = request.getParameter(CONTACT_ID);
        if (contactId != null) {
            Long specifiedId = Long.parseLong(contactId);
            context.setAttribute(CONTACT_ID, specifiedId);
            Contact specifiedContact = contactDAO.find(specifiedId);
            context.setAttribute("name", specifiedContact.getName());
            context.setAttribute("phoneNumber", specifiedContact.getPhoneNumber());
            context.setAttribute("extraInfo", specifiedContact.getExtraInfoMap());
        }
        context.getRequestDispatcher("/WEB-INF/overview.jsp").forward(request, response);
    }

    public void destroy() {
    }
}