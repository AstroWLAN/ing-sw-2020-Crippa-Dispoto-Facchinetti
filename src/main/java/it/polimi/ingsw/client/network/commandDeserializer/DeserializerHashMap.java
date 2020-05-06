package it.polimi.ingsw.client.network.commandDeserializer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import it.polimi.ingsw.client.network.commands.*;
import it.polimi.ingsw.client.network.commands.allPhases.BattlefieldCommands;
import it.polimi.ingsw.client.network.commands.lobbyPhase.*;
import it.polimi.ingsw.client.network.commands.matchPhase.ActualPlayerCommand;
import it.polimi.ingsw.client.network.commands.matchPhase.SetStartTurnResponse;
import it.polimi.ingsw.client.network.commands.matchPhase.WorkerViewUpdateCommand;
import it.polimi.ingsw.client.network.data.dataInterfaces.CellInterface;
import it.polimi.ingsw.client.network.data.dataInterfaces.CellMatrixInterface;
import it.polimi.ingsw.client.network.data.dataInterfaces.PlayerInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class  DeserializerHashMap  {
    private Map<String, ProcessingCommand> commandMap;
    private static DeserializerHashMap instance = null;

    /**
     * Factory method that returns the DeserializerHashMap instance (Singleton)
     * @return DeserializerHashMap object
     */
    public static DeserializerHashMap getDeserializerHashMapInstance(){
        if(null == instance){
            instance = new DeserializerHashMap();
            instance.initializeDeserializerHashMap();
        }
        return instance;
    }

    /** Get Specific Command from the commandMap
     *
     * @param action    key of the command requested
     * @return  ProcessingCommand object
     */
    public ProcessingCommand getMapCommand(String action){
        return this.commandMap.get(action);
    }

    /**
     *  Initialize the map with the commands present in the loading phase
     */
    private void initializeDeserializerHashMap() {
        this.commandMap = new HashMap<>();
        //Load Specific Deserialization Methods in commandMap
        loadAddPlayerResponse();
        loadSetPickedCards();
        loadGetDeckResponse();
        loadSetPlayerCard();
        loadSetWorkersPosition();
        loadGetPlayersResponse();
        loadGetBattlefieldResponse();
        loadBattlefieldUpdate();

        loadActualPlayer();
        loadSetStartTurnResponse();

        loadWorkerViewUpdate();
    }

    //Specific Deserialization Methods

    //1
    private void loadAddPlayerResponse(){
        this.commandMap.put("addPlayerResponse", new ProcessingCommand() {
            public Command command(JsonElement jsonElement) {
                return  new AddPlayerCommand(
                        jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("validNick").getAsBoolean(),
                        jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("lobbyState").getAsBoolean(),
                        jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("lobbySize").getAsInt(),
                        jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("fullLobby").getAsBoolean());
            }
        });
    }

    //2
    private void loadSetPickedCards(){
        this.commandMap.put("setPickedCards", new ProcessingCommand() {
            public Command command(JsonElement jsonElement) {
                return  new SetPickedCardsCommand(
                        jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("playerNickname").getAsString());
            }
        });
    }

    //3
    private void loadGetDeckResponse(){
        this.commandMap.put("getDeckResponse", new ProcessingCommand() {
            public Command command(JsonElement jsonElement) {
                return  new Gson().fromJson(jsonElement.getAsJsonObject().get("data"), GetDeckCommand.class);
            }
        });
    }

    //4
    private void loadSetPlayerCard(){
        this.commandMap.put("setPlayerCard", new ProcessingCommand() {
            public Command command(JsonElement jsonElement) {
                return  new Gson().fromJson(jsonElement.getAsJsonObject().get("data"), SetPlayerCardCommand.class);
            }
        });
    }

    //5
    private void loadSetWorkersPosition(){
        this.commandMap.put("setWorkersPosition", new ProcessingCommand() {
            public Command command(JsonElement jsonElement) {
                return  new Gson().fromJson(jsonElement.getAsJsonObject().get("data"), SetWorkersPositionCommand.class);
            }
        });
    }

    //6
    private void loadGetPlayersResponse(){
        this.commandMap.put("getPlayersResponse", new ProcessingCommand() {
            public Command command(JsonElement jsonElement) {
                return  new GetPlayersCommand(deserializePlayers(jsonElement));
            }
        });
    }

    //7
    private void loadGetBattlefieldResponse(){
        this.commandMap.put("getBattlefieldResponse", new ProcessingCommand() {
            public Command command(JsonElement jsonElement) {
                return  new BattlefieldCommands(deserializeCellMatrix(jsonElement), "getBattlefieldResponse");
            }
        });
    }

    //8
    private void loadBattlefieldUpdate(){
        this.commandMap.put("battlefieldUpdate", new ProcessingCommand() {
            public Command command(JsonElement jsonElement) {
                return  new BattlefieldCommands(deserializeCellMatrix(jsonElement), "battlefieldUpdate");
            }
        });
    }

    //9
    private void loadActualPlayer(){
        this.commandMap.put("actualPlayer", new ProcessingCommand() {
            public Command command(JsonElement jsonElement) {
                return  new ActualPlayerCommand(
                        jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("playerNickname").getAsString());
            }
        });
    }

    //10
    private void loadSetStartTurnResponse(){
        this.commandMap.put("setStartTurnResponse", new ProcessingCommand() {
            public Command command(JsonElement jsonElement) {
                return  new Gson().fromJson(jsonElement.getAsJsonObject().get("data"), SetStartTurnResponse.class);
            }
        });
    }

    //10
    private void loadWorkerViewUpdate(){
        this.commandMap.put("workerViewUpdate", new ProcessingCommand() {
            public Command command(JsonElement jsonElement) {
                return  new WorkerViewUpdateCommand(deserializeCellMatrix(jsonElement));
            }
        });
    }


    //Common Methods
    private CellInterface[][] deserializeCellMatrix (JsonElement jsonElement){
        CellMatrixInterface cellMatrix = new Gson().fromJson(jsonElement.getAsJsonObject().get("data"), CellMatrixInterface.class);
        return cellMatrix.getCellMatrix();
    }
    private List<PlayerInterface> deserializePlayers (JsonElement jsonElement){
        List<PlayerInterface> players = new ArrayList<>();
        //Start Deserialization of jsonElement
        JsonArray jsonArray = jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("players").getAsJsonArray();
        for(JsonElement jsonE : jsonArray) {
            players.add(new Gson().fromJson(jsonE, PlayerInterface.class));
        }
        return players;
    }

}
