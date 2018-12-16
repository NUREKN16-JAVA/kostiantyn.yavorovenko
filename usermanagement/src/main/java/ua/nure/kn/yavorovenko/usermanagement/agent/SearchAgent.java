package ua.nure.kn.yavorovenko.usermanagement.agent;

import jade.core.Agent;

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
}
