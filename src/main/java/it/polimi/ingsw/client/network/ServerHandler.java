package it.polimi.ingsw.client.network;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.network.actions.CommandFactory;

/**
 * ServerHandler get server response and send command to server
 */
public class ServerHandler{
    private final ClientController clientController;
    private final ServerThread thread;

    /**
     * Create ServerHandler
     * @param clientController controller
     * @param thread thread
     */
    public ServerHandler(ClientController clientController, ServerThread thread){
       this.clientController = clientController;
       this.thread = thread;
       clientController.registerHandler(this);
    }

    /**
     * Process command received from the server
     * @param m message
     */
    public void process(String m){
        CommandFactory.from(m).execute(this.clientController);
    }

    /**
     * Send request message to server
     * @param m message
     */
    public void request (String m){
        this.thread.send(m);
    }
}
