package ua.nure.kn.yavorovenko.usermanagement.agent;

import jade.core.AID;
import jade.core.Agent;
import ua.nure.kn.yavorovenko.usermanagement.User;
import ua.nure.kn.yavorovenko.usermanagement.db.DaoFactory;
import ua.nure.kn.yavorovenko.usermanagement.db.DatabaseException;

import java.util.Collection;


public class SearchAgent extends Agent {
    @Override
    protected void setup() {
        super.setup();
        System.out.println(getAID().getName() + " started.");
    }

    @Override
    protected void takeDown() {
        super.takeDown();
        System.out.println(getAID().getName() + " terminated.");
    }

    public void search(String firstName, String lastName) throws SearchException {
        try {
            Collection<User> users = DaoFactory.getInstance().getUserDao().find(firstName, lastName);
            if (users.size() > 0) {
                showUsers(users);
            } else {
                addBehaviour(new SearchRequestBehaviour(new AID[] {}, firstName, lastName ));
            }
        } catch (DatabaseException e) {
            throw new SearchException(e);
        }
    }

    public void showUsers(Collection<User> users) {

    }
}
