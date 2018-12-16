package ua.nure.kn.yavorovenko.usermanagement.agent;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class SearchRequestBehaviour extends Behaviour {

    private static final String STRING_SEPARATOR = ",";

    private AID[] aids;
    private String firstName;
    private String lastName;

    public SearchRequestBehaviour(AID[] aid, String firstName, String lastName) {
        this.aids = aid;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public void action() {
        ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
        message.setContent(firstName + STRING_SEPARATOR + lastName);
        if (aids != null) {
            for (int i = 0; i < aids.length; i++) {
                message.addReceiver(aids[i]);
            }
            myAgent.send(message);
        }
    }

    @Override
    public boolean done() {
        return true;
    }
}
