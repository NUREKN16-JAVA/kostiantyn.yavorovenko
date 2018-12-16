package ua.nure.kn.yavorovenko.usermanagement.web;

import ua.nure.kn.yavorovenko.usermanagement.User;
import ua.nure.kn.yavorovenko.usermanagement.db.DaoFactory;
import ua.nure.kn.yavorovenko.usermanagement.db.DatabaseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public class BrowseServlet extends HttpServlet {

    private static final long serialVersionUID = -4683563138736520242L;
    private static final String ATTR_USERS = "users";
    private static final String BROWSE_PAGE = "/browse.jsp";
    private static final String EDIT_PAGE = "/edit.jsp";
    private static final String ADD_PAGE = "add.jsp";

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("addButton") != null) {
            add(req, resp);
        } else if (req.getParameter("editButton") != null) {
            edit(req, resp);
        } else if (req.getParameter("deleteButton") !=  null) {
            delete(req, resp);
        } else if (req.getParameter("detailsButton") != null) {
            details(req, resp);
        } else {
            browse(req, resp);
        }
    }

    private void browse(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Collection<User> collectionOfUsers;
        try {
            collectionOfUsers = DaoFactory.getInstance().getUserDao().findAll();
            req.getSession().setAttribute(ATTR_USERS, collectionOfUsers);
            req.getRequestDispatcher(BROWSE_PAGE).forward(req, resp);
        } catch (DatabaseException e) {
            throw new ServletException("Can not get users from DB", e);
        }
    }

    private void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(ADD_PAGE).forward(req, resp);
    }
    private void edit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStrUser = req.getParameter("id");

        if (idStrUser == null || idStrUser.trim().isEmpty()) {
            req.setAttribute("error", "You must select a user");
            req.getRequestDispatcher(BROWSE_PAGE).forward(req, resp);
            return;
        }

        try {
            User foundUser = DaoFactory.getInstance().getUserDao().find(new Long(idStrUser));
            req.getSession().setAttribute("user", foundUser);
        } catch (DatabaseException e) {
            req.setAttribute("error", "ERROR"  + e.toString());
            req.getRequestDispatcher(BROWSE_PAGE).forward(req, resp);
            return;
        }
        req.getRequestDispatcher(EDIT_PAGE).forward(req, resp);
    }
    private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
    private void details(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }


}
