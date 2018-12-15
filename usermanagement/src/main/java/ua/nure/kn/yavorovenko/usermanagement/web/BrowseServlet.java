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

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        browse(req, resp);
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


}
