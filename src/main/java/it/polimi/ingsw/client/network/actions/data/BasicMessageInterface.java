package it.polimi.ingsw.client.network.actions.data;

public class BasicMessageInterface {
    private String action;
    private Object data;

    public BasicMessageInterface(String action, Object data){
        this.action = action;
        this.data = data;
    }
}
