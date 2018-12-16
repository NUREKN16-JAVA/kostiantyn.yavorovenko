package ua.nure.kn.yavorovenko.usermanagement.web;

import ua.nure.kn.yavorovenko.usermanagement.User;
import ua.nure.kn.yavorovenko.usermanagement.db.DaoFactory;
import ua.nure.kn.yavorovenko.usermanagement.db.DatabaseException;
import ua.nure.kn.yavorovenko.usermanagement.web.exceptions.ValidationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;

public class EditServlet extends HttpServlet {
    private static final long serialVersionUID = -2274888176643905731L;
    private static final String EDIT_PAGE = "/edit.jsp";
    private static final String BROWSE_PAGE = "/browse.jsp";

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
        if (req.getParameter("okButton") != null) {
            doOk(req, resp);
        } else if (req.getParameter("cancelButton") != null) {
            doCancel(req, resp);
        } else {
            showPage(req, resp);
        }
    }

    private void showPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(EDIT_PAGE).forward(req, resp);
    }

    private void doCancel(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(BROWSE_PAGE).forward(req, resp);
    }

    private void doOk(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = null;
        try {
            user = getUserFromRequest(req);
        } catch (ValidationException e) {
            req.setAttribute("error", e.getMessage());
            showPage(req, resp);
            return;
        }
        try {
            processUser(user);
        } catch (DatabaseException e) {
            throw new ServletException("Could not update users in DB", e);
        }
        req.getRequestDispatcher("/browse").forward(req, resp);
    }

    private void processUser(User user) throws DatabaseException {
        DaoFactory.getInstance().getUserDao().update(user);
    }

    public User getUserFromRequest(HttpServletRequest req) throws ValidationException {
        User user = new User();
        String idString = req.getParameter("id");
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String dateOfBirthString = req.getParameter("dateOfBirth");

        if (idString == null) {
            throw new ValidationException("ID is empty!");
        }

        if (firstName == null) {
            throw new ValidationException("First name is empty!");
        }

        if (lastName == null) {
            throw new ValidationException("Last name is empty!");
        }

        if (dateOfBirthString == null) {
            throw new ValidationException("Date of birth is empty!");
        }

        user.setId(new Long(idString));
        user.setFirstName(firstName);
        user.setLastName(lastName);

        try {
            user.setDateOfBirth(DateFormat.getDateInstance().parse(dateOfBirthString));
        } catch (ParseException e) {
            throw new ValidationException("Date format is incorrect!", e);
        }

        return user;
    }
}
