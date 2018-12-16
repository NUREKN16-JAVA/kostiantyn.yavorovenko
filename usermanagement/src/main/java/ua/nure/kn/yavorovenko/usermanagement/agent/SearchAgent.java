package ua.nure.kn.yavorovenko.usermanagement.agent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import ua.nure.kn.yavorovenko.usermanagement.User;
import ua.nure.kn.yavorovenko.usermanagement.db.DaoFactory;
import ua.nure.kn.yavorovenko.usermanagement.db.DatabaseException;

import java.util.Collection;


public class SearchAgent extends Agent {
    private static final int ONE_MINUTE_IN_MLSECONDS = 60000;

    private AID[] aids;

    @Override
    protected void setup() {
        super.setup();
        System.out.println(getAID().getName() + " started.");

        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        dfAgentDescription.setName(getAID());

        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setName("JADE-searching");
        serviceDescription.setType("searching");
        dfAgentDescription.addServices(serviceDescription);

        try {
            DFService.register(this, dfAgentDescription);
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        addBehaviour(new TickerBehaviour(this, ONE_MINUTE_IN_MLSECONDS) {
            @Override
            protected void onTick() {
                DFAgentDescription agentDescription = new DFAgentDescription();
                ServiceDescription serviceDescription = new ServiceDescription();

                serviceDescription.setType("searching");
                agentDescription.addServices(serviceDescription);

                try {
                    DFAgentDescription[] descriptions =
                            DFService.search(myAgent, agentDescription);
                    aids = new AID[descriptions.length];

                    for (int i = 0; i < descriptions.length; i++) {
                        DFAgentDescription description = descriptions[i];
                        aids[i] = description.getName();
                    }
                } catch (FIPAException e) {
                    e.printStackTrace();
                }
            }
        });
        
        addBehaviour(new RequestServer());

    }

    @Override
    protected void takeDown() {
        System.out.println(getAID().getName() + " terminated.");
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        super.takeDown();
    }

    public void search(String firstName, String lastName) throws SearchException {
        try {
            Collection<User> users = DaoFactory.getInstance().getUserDao().find(firstName, lastName);
            if (users.size() > 0) {
                showUsers(users);
            } else {
                addBehaviour(new SearchRequestBehaviour(aids, firstName, lastName));
            }
        } catch (DatabaseException e) {
            throw new SearchException(e);
        }
    }

    void showUsers(Collection<User> users) {

    }
}
