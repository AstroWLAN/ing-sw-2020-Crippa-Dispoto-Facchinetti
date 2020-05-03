package it.polimi.ingsw.client.network.actions;

import it.polimi.ingsw.client.controller.ClientController;

import java.util.List;

public class SetPlayerCardCommand implements Command {
    List<String> cards;

    @Override
    public void execute(ClientController clientController) {
        clientController.setGodCards(cards);
        //Awakens who was waiting Server Response
        synchronized (clientController.waitManager.waitSetPlayerCard){
            clientController.waitManager.waitSetPlayerCard.setUsed();
            clientController.waitManager.waitSetPlayerCard.notify();
        }
    }
}
