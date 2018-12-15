package ua.nure.kn.yavorovenko.usermanagement.web;

import ua.nure.kn.yavorovenko.usermanagement.User;
import ua.nure.kn.yavorovenko.usermanagement.db.DaoFactory;
import ua.nure.kn.yavorovenko.usermanagement.db.DatabaseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;

public class EditServlet extends HttpServlet {
    private static final long serialVersionUID = -2274888176643905731L;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
        if (req.getParameter("okButton") != null) {
            doOk(req, resp);
        }else if (req.getParameter("cancelButton") != null) {
            doCancel(req, resp);
        }else{
            showPage();
        }
    }

    private void showPage() {

    }

    private void doCancel(HttpServletRequest req, HttpServletResponse resp) {

    }

    private void doOk(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getUserFromRequest(req);
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

    public User getUserFromRequest(HttpServletRequest req) {
        String idString = req.getParameter("id");
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String dateOfBirthString = req.getParameter("dateOfBirth");

        User user = null;

        if (idString != null || firstName != null || lastName != null || dateOfBirthString != null) {
            try {
                user = new User(
                        new Long(idString),
                        firstName,
                        lastName,
                        DateFormat.getDateInstance().parse(dateOfBirthString)
                );
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return user;
    }
}
