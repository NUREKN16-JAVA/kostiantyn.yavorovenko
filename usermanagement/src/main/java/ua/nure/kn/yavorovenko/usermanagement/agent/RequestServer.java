package ua.nure.kn.yavorovenko.usermanagement.agent;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import ua.nure.kn.yavorovenko.usermanagement.User;
import ua.nure.kn.yavorovenko.usermanagement.db.DaoFactory;
import ua.nure.kn.yavorovenko.usermanagement.db.DatabaseException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

public class RequestServer extends CyclicBehaviour {

    private static final String STRING_SEPARATOR = ",";
    private static final int VALID_COUNT_TOKENS_IN_MSG = 2;
    private static final int SIZE_OF_EMPTY_LIST = 0;

    @Override
    public void action() {
        ACLMessage message = myAgent.receive();
        if (message != null) {
            if (message.getPerformative() == ACLMessage.REQUEST) {
                myAgent.send(createReply(message));
            } else {
                Collection<User> users = parseMessage(message);
                ((SearchAgent) myAgent).showUsers(users);
            }
        } else {
            block();
        }
    }

    private Collection<User> parseMessage(ACLMessage message) {
        return null;
    }

    private ACLMessage createReply(ACLMessage message) {
        ACLMessage reply = message.createReply();
        String content = message.getContent();
        StringTokenizer tokenizer = new StringTokenizer(content, STRING_SEPARATOR);
        if (tokenizer.countTokens() == VALID_COUNT_TOKENS_IN_MSG) {
            String firstName = tokenizer.nextToken();
            String lastName = tokenizer.nextToken();
            Collection<User> users = null;
            try {
                users = DaoFactory.getInstance().getUserDao().find(firstName, lastName);
            } catch (DatabaseException e) {
                e.printStackTrace();
                users = new ArrayList<>(SIZE_OF_EMPTY_LIST);
            }

            StringBuffer buffer = new StringBuffer();
            for (Iterator<User> it = users.iterator(); it.hasNext();) {
                User user = it.next();
                buffer.append(user.getId()).append(STRING_SEPARATOR);
                buffer.append(user.getFirstName()).append(STRING_SEPARATOR);
                buffer.append(user.getLastName()).append(STRING_SEPARATOR);
            }
            reply.setContent(buffer.toString());
        }

        return reply;
    }
}
