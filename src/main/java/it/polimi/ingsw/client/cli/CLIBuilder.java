package it.polimi.ingsw.client.cli;
import it.polimi.ingsw.client.clientModel.basic.Color;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.UIActions;
import it.polimi.ingsw.client.network.actions.data.dataInterfaces.PlayerInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * CLIBuilder contains everything you need to build the CLI and use it
 */
public class CLIBuilder implements UIActions {

    //ANSI Colors
    private static final String CODE_BLUE ="33";
    private static final String CODE_LIGHTBLUE ="75";
    private static final String CODE_BROWN ="130";
    private static final String CODE_GRAY ="252";
    private static final String CODE_WHITE ="255";
    private static final String CODE_RED = "197";
    private static final String CODE_PURPLE = "105";
    private static final String CODE_GREEN = "41";
    private static final String CODE_BLACK = "232";
    private static final String ANSI_PRFX ="\u001b[38;5;";
    protected static final String ANSI_RST = "\u001b[0m";

    protected static final String ANSI_BLUE = ANSI_PRFX+CODE_BLUE+"m";
    protected static final String ANSI_LIGHTBLUE = ANSI_PRFX+CODE_LIGHTBLUE+"m";
    protected static final String ANSI_BROWN = ANSI_PRFX+CODE_BROWN+"m";
    protected static final String ANSI_GRAY = ANSI_PRFX+CODE_GRAY+"m";
    protected static final String ANSI_WHITE = ANSI_PRFX+CODE_WHITE+"m";
    protected static final String ANSI_PURPLE = ANSI_PRFX+CODE_PURPLE+"m";
    protected static final String ANSI_RED = ANSI_PRFX+CODE_RED+"m";
    protected static final String ANSI_GREEN = ANSI_PRFX+CODE_GREEN+"m";
    protected static final String ANSI_BLACK = ANSI_PRFX+CODE_BLACK+"m";
    private static String COLORMODE;

    //Game data
    private CLIDataObject[] boardCellsContents; // row ┃   ┃   ┃   ┃   ┃   ┃
    private List<String> playerMoves;
    private String currentPhase;
    private int numberFullTowers;

    //UI Objects
    protected static final String CLI_INPUT = "> ";
    protected static final String NEW_LINE = "\n";
    protected static final String BLANK = " ";
    private static final String BOARD_TITLE = "BOARD";
    private static final String PLAYERS_TITLE = "PLAYERS";
    private static final String TOWERS_TITLE = "FULL TOWERS";
    private static final String MOVES_TITLE = "PLAYER MOVES";
    private static final String PHASE_TITLE = "CURRENT PHASE";

    //Web utilities
    private static final String SETUPTITLE = "Setup Connection";

    private static final String SERVER_IP = "Server IP 🌍";
    private static final String NICKNAME = "Nickname 👾";
    private static final String SETPLAYERS = "Number of players 👦🏼";
    private static final String LOBBY_SIZE = "Lobby Size 📦";
    private static final String LOBBY_JOIN = "Joining the lobby...⛩";
    private static final String CONNECTION_HUNT = "Connection lost! Looking for connection...🦖";
    private static final String HANDSHAKING = "Handshaking with %s on port %s...🦖 ";

    private static final String SUCCESS_HANDSHAKING = "Connection established!";
    private static final String SUCCESS_LOBBY_ACCESS = "You have correctly joined the lobby!";

    private static final String HANDSHAKING_ERROR = "Invalid IP...retry! • ";
    private static final String LOBBY_SIZE_ERROR = "This game is just for 2 or 3 people...retry! • ";
    private static final String UNAVAILABLE_LOBBY = "The selected lobby is unavailable";
    private static final String NICKNAME_ERROR = "There is already a player with this nickname in the lobby...retry!";


    private static final String WAITSTART = "Wait for the match startup...";
    private static final String CLIENT_MIGRATION = "Migrating to the other one...";
    private HashMap<Integer,Color> ColorsSetupMap;

    //Players Information Box
    protected static final String WORKER = "◈";
    private HashMap<Color,String> WorkerColorsMap;

    //Cards
    private static final String cardTemplate = "• %s | %s";

    //Board Matrix
    protected static final String L_T_CORNER = "┏";
    protected static final String R_T_CORNER = "┓";
    protected static final String L_B_CORNER = "┗";
    protected static final String R_B_CORNER = "┛";
    protected static final String R_LAT_SEPARATOR = "┫";
    protected static final String L_LAT_SEPARATOR = "┣";
    protected static final String INT_SEPARATOR = "╋";
    protected static final String U_LAT_SEPARATOR = "┳";
    protected static final String LO_LAT_SEPARATOR = "┻";
    protected static final String H_LINE = "━";
    protected static final String V_LINE = "┃";

    //Messages Box
    protected static final String L_THIN_T_CORNER = "┌";
    protected static final String R_THIN_T_CORNER = "┐";
    protected static final String L_THIN_B_CORNER = "└";
    protected static final String R_THIN_B_CORNER = "┘";
    protected static final String H_THIN_LINE = "─";
    protected static final String V_THIN_LINE = "│";
    protected static final String DOT_H_LINE = "╌";
    protected static final String DOT_V_LINE = "┊";

    //ANSI Cursor Moves
    protected static final String CURSOR_UP = "\u001b[%sA";
    protected static final String CURSOR_DWN = "\u001b[%sB";
    protected static final String CURSOR_LFT = "\u001b[%sD";
    protected static final String CURSOR_RGT = "\u001b[%sC";

    //ANSI Special Sequences
    protected static final String CLEAN = "\u001b[0J";

    //Templates
    private static final String upperEdgeBoard =
            L_T_CORNER+H_LINE+H_LINE+H_LINE+U_LAT_SEPARATOR+
            H_LINE+H_LINE+H_LINE+U_LAT_SEPARATOR+
            H_LINE+H_LINE+H_LINE+U_LAT_SEPARATOR+
            H_LINE+H_LINE+H_LINE+U_LAT_SEPARATOR+
            H_LINE+H_LINE+H_LINE+R_T_CORNER;
    private static final String intermediateEdgeBoard =
            L_LAT_SEPARATOR+H_LINE+H_LINE+H_LINE+INT_SEPARATOR+
            H_LINE+H_LINE+H_LINE+INT_SEPARATOR+
            H_LINE+H_LINE+H_LINE+INT_SEPARATOR+
            H_LINE+H_LINE+H_LINE+INT_SEPARATOR+
            H_LINE+H_LINE+H_LINE+R_LAT_SEPARATOR;
    private static final String lowerEdgeBoard =
            L_B_CORNER+H_LINE+H_LINE+H_LINE+LO_LAT_SEPARATOR+
            H_LINE+H_LINE+H_LINE+LO_LAT_SEPARATOR+
            H_LINE+H_LINE+H_LINE+LO_LAT_SEPARATOR+
            H_LINE+H_LINE+H_LINE+LO_LAT_SEPARATOR+
            H_LINE+H_LINE+H_LINE+R_B_CORNER;
    private static final String edge_distance = BLANK+BLANK+BLANK;
    private static final String playerInformations = " %s %s|%s "; // ᳵ SteveJobs|Athena
    private static final String playerMove = " [%s|%s] "; // [1|2]

    //Sizes
    private int rowCounter;
    private final int refreshable_area_height = 15;
    private final int editable_board_rows = 5;


    /**
     * Class Constructor
     */
    public CLIBuilder(String colorMode) {
        this.boardCellsContents = new CLIDataObject[5];
        this.playerMoves = new ArrayList<>();
        this.ColorsSetupMap = new HashMap<>();
        this.WorkerColorsMap = new HashMap<>();
        this.currentPhase = null;
        this.numberFullTowers = 0;
        this.rowCounter = 0;
        //Initial color scheme setup
        if(colorMode.equals("light")){
            COLORMODE=ANSI_BLACK;
        }
        else{
            COLORMODE=ANSI_WHITE;
        }
        //Colors for the setup connection phase <NumbersOfPlayersInTheLobby, AssociatedColor>
        ColorsSetupMap.put(0,Color.BLUE);
        ColorsSetupMap.put(1,Color.GREY);
        ColorsSetupMap.put(2,Color.BROWN);
        //Generate colored workers based on the player color
        WorkerColorsMap.put(Color.BLUE,ANSI_BLUE+WORKER);
        WorkerColorsMap.put(Color.BROWN,ANSI_BROWN+WORKER);
        WorkerColorsMap.put(Color.GREY,ANSI_GRAY+WORKER);
    }

    /**
     * Prints a message in a colored box
     * @param message is the string that has to be print
     * @param messageColor is the color of the box and the message
     */
    public void renderMessageBox(String messageColor, String message){
        int messageLength = message.length();
        System.out.print(messageColor+L_THIN_T_CORNER);
        //+2 to consider the blank spaces between the message and the lateral edges
        for(int i=0;i<messageLength+2;i++)
            System.out.print(H_THIN_LINE);
        System.out.println(R_THIN_T_CORNER);
        System.out.println(V_THIN_LINE+BLANK+message+BLANK+V_THIN_LINE);
        System.out.print(L_THIN_B_CORNER);
        for(int i=0;i<messageLength+2;i++)
            System.out.print(H_THIN_LINE);
        System.out.println(R_THIN_B_CORNER);
    }

    /**
     * Renders the players information box
     *      PLAYERS
     *      ┌────────────────────┐
     *      │ ◈ SteveJobs|Athena │
     *      │ ...                │
     *      └────────────────────┘
     */
    public void renderPlayersInfoBox(ClientController clientController){
        int maxLength = 0;
        String pieceOfString;
        List<String> playerInfo = new ArrayList<>();
        StringBuilder playerData = new StringBuilder();
        System.out.print(COLORMODE+PLAYERS_TITLE+NEW_LINE);
        clientController.getPlayersRequest();
        //Build the players info strings
        for(PlayerInterface current : clientController.getPlayers()){
            pieceOfString= WorkerColorsMap.get(current.getColor());
            playerData.append(V_THIN_LINE + BLANK).append(pieceOfString);
            pieceOfString = current.getPlayerNickname();
            playerData.append(BLANK).append(COLORMODE).append(pieceOfString);
            pieceOfString = current.getCard();
            playerData.append(BLANK+"•"+BLANK).append(pieceOfString).append(BLANK+V_THIN_LINE);
            playerInfo.add(playerData.toString());}
        //Discover the longest string
        for(String current : playerInfo){
            if(current.length()>maxLength)
                maxLength=current.length();}
        //Print the box
        System.out.print(BLANK+BLANK+BLANK+COLORMODE+PLAYERS_TITLE+NEW_LINE);
        System.out.print(BLANK+BLANK+BLANK+L_THIN_T_CORNER);
        for(int i=0;i<maxLength;i++)
            System.out.print(H_THIN_LINE);
        System.out.print(R_THIN_T_CORNER+NEW_LINE);
        for(String current : playerInfo){
            System.out.print(BLANK+BLANK+BLANK+current+NEW_LINE);
        }
        System.out.print(BLANK+BLANK+BLANK+L_THIN_B_CORNER);
        for(int i=0;i<maxLength;i++)
            System.out.print(H_THIN_LINE);
        System.out.print(R_THIN_B_CORNER+NEW_LINE+NEW_LINE);
    }

    /**
     * Renders the main part of the CLI
     *            BOARD                             0
     *      0   1   2   3   4     FULL TOWERS       1
     *    ┏━━━┳━━━┳━━━┳━━━┳━━━┓   ┌╌╌╌┐             2
     *  0 ┃   ┃   ┃   ┃   ┃   ┃   ┊ 4 ┊             3
     *    ┣━━━╋━━━╋━━━╋━━━╋━━━┫   └╌╌╌┘             4
     *  1 ┃   ┃   ┃   ┃   ┃   ┃   CURRENT PHASE     5
     *    ┣━━━╋━━━╋━━━╋━━━╋━━━┫   ┌╌╌╌╌╌╌╌╌╌╌┐      6
     *  2 ┃   ┃   ┃   ┃   ┃   ┃   ┊ Building ┊      7
     *    ┣━━━╋━━━╋━━━╋━━━╋━━━┫   └╌╌╌╌╌╌╌╌╌╌┘      8
     *  3 ┃   ┃   ┃   ┃   ┃   ┃   AVAILABLE MOVES   9
     *    ┣━━━╋━━━╋━━━╋━━━╋━━━┫   ┌╌╌╌╌╌╌╌╌╌╌╌╌╌┐   10
     *  4 ┃   ┃   ┃   ┃   ┃   ┃   ┊ [2|1] [0|2] ┊   11
     *    ┗━━━┻━━━┻━━━┻━━━┻━━━┛   └╌╌╌╌╌╌╌╌╌╌╌╌╌┘   12
     *                                              13
     * > Type something...                          14 + 1 (press Enter)
     */
    public void renderCLI(){}

    /**
     *  Renders the available cards for the initial player choice
     *  • Chronos | Owner win if there are five full towers on the board
     */
    public void renderAvailableCards(){};

    /**
     * Renders the entire deck (list of cards)
     */
    public void renderDeck(){
    };


    //UI ACTION METHODS
    @Override
    public void pickCards(ClientController clientController) {

    }

    @Override
    public void chooseCard(ClientController clientController) {

    }

    @Override
    public void placeWorkers(ClientController clientController) {

    }

    @Override
    public void selectWorker(ClientController clientController) {

    }

    @Override
    public void moveWorker(ClientController clientController) {

    }

    @Override
    public void buildBlock(ClientController clientController) {

    }

    @Override
    public void removeBlock(ClientController clientController) {

    }

    @Override
    public void skipAction(ClientController clientController) {

    }

    @Override
    public void showCards(ClientController clientController) {

    }

    @Override
    public void setupConnection(ClientController clientController) {
        Scanner consoleScanner = new Scanner(System.in);
        String userInput;
        String chosenNickname;
        Color generatedColor;
        int userValue = 0;
        int chosenLobby;
        boolean validOperation = false;
        boolean validUsername = true;
        renderMessageBox(ANSI_PURPLE,SETUPTITLE);
        /*  # Server Handshake #
            Server IP 🌍
            >
            🦖 Handshaking with 192.168.1.9 on port 1337...|
         */
        System.out.print(COLORMODE+SERVER_IP+NEW_LINE+CLI_INPUT);
        userInput=consoleScanner.nextLine();
        clientController.getSocketConnection().setServerName(userInput);
        System.out.print(String.format(HANDSHAKING,clientController.getSocketConnection().getServerName(),clientController.getSocketConnection().getServerPort())+NEW_LINE);
        while(!validOperation){
            if(!clientController.getSocketConnection().startConnection()){
                /*  # Handshake error #
                    Invalid IP...retry! • Server IP 🌍
                    > |
                */
                System.out.print(String.format(CURSOR_UP,2));
                System.out.print(CLEAN);
                System.out.print(ANSI_RED+HANDSHAKING_ERROR+COLORMODE+SERVER_IP+NEW_LINE+CLI_INPUT);
                userInput=consoleScanner.nextLine();
                clientController.getSocketConnection().setServerName(userInput);
                System.out.print(String.format(HANDSHAKING,clientController.getSocketConnection().getServerName(),clientController.getSocketConnection().getServerPort())+NEW_LINE);
                /*  # Handshake Retry #
                    Invalid IP...retry! • Server IP 🌍
                    >
                    🦖 Handshaking with 192.168.1.9 on port 1337...|
                */

            }
            else{
                /*  # Handshake success #
                    Server IP 🌍
                    >
                    🦖 Handshaking with 192.168.1.9 on port 1337...
                    Connection established!
                    |
                */
                validOperation=true;
                System.out.print(ANSI_GREEN+SUCCESS_HANDSHAKING+NEW_LINE);
            }
        }
         /*  # Nickname setup #
            Server IP 🌍
            >
            🦖 Handshaking with 192.168.1.9 on port 1337...
            Connection established!
            Nickname 👾
            > |
        */
        System.out.print(COLORMODE+NICKNAME+NEW_LINE+CLI_INPUT);
        userInput=consoleScanner.nextLine();
        clientController.setPlayerNickname(userInput);
        /*  # Lobby choice #
            Server IP 🌍
            >
            Handshaking with 192.168.1.9 on port 1337...
            Connection established!
            Nickname 👾
            >
            Lobby Size 📦
            >
        */
        System.out.print(LOBBY_SIZE+NEW_LINE+CLI_INPUT);
        userValue=consoleScanner.nextInt();
        //The only valid choices are 2 or 3
        if(userValue!=2 && userValue!=3)
            validOperation=false;
        else
            validOperation=true;
        while(!validOperation){
            /*  # Wrong Lobby Size #
                Server IP 🌍
                >
                Handshaking with 192.168.1.9 on port 1337...
                Connection established!
                Nickname 👾
                >
                This game is just for 2 or 3 people...retry! • Lobby Size 📦
                >
            */
            System.out.print(String.format(CURSOR_UP,2));
            System.out.print(CLEAN);
            System.out.print(ANSI_RED+LOBBY_SIZE_ERROR+COLORMODE+LOBBY_SIZE+NEW_LINE+CLI_INPUT);
            userValue=consoleScanner.nextInt();
            if(userValue!=2 && userValue!=3)
                validOperation=false;
            else
                validOperation=true;
        }
        //Save user preferences
        chosenLobby=userValue;
        chosenNickname=clientController.getPlayerNickname();
        //Adds the player to the lobby
        clientController.addPlayerRequest(clientController.getPlayerNickname(),userValue);
        System.out.print(LOBBY_JOIN+NEW_LINE);
        //Troubles with the lobby...
        while(!clientController.getValidNick() || !clientController.getLobbyState()){

            /*  # Nickname Unavailable # -> There is a player with the same nickname in the lobby
                Server IP 🌍
                >
                Handshaking with 192.168.1.9 on port 1337...
                Connection established!
                Nickname 👾
                >
                Lobby Size 📦
                >
                Joining the lobby...⛩
                There is already a player with this nickname in the lobby...retry!
                > |
            */
            if(!clientController.getValidNick()){
                System.out.print(ANSI_RED+NICKNAME_ERROR+NEW_LINE+ANSI_WHITE+CLI_INPUT);
                userInput=consoleScanner.nextLine();
                clientController.setPlayerNickname(userInput);
                clientController.addPlayerRequest(clientController.getPlayerNickname(),chosenLobby);
                System.out.print(String.format(CURSOR_UP,2));
                System.out.print(CLEAN);
            }
            /*  # Lobby Unavailable # -> There are troubles with the selected lobby
                Server IP 🌍
                >
                Handshaking with 192.168.1.9 on port 1337...
                Connection established!
                Nickname 👾
                >
                Lobby Size 📦
                >
                Joining the lobby...⛩
                The selected lobby is unavailable
                > Migrating to the other one...|
            */
            else{
                System.out.print(ANSI_RED+UNAVAILABLE_LOBBY+NEW_LINE+ANSI_WHITE+CLI_INPUT+CLIENT_MIGRATION);
                if(chosenLobby==2){
                    clientController.addPlayerRequest(chosenNickname,3);
                    System.out.print(String.format(CURSOR_UP,2));
                    System.out.print(CLEAN);
                }
                else{
                    clientController.addPlayerRequest(chosenNickname,2);
                    System.out.print(String.format(CURSOR_UP,2));
                    System.out.print(CLEAN);
                }
            }
        }

        /*  # Setup Done! #
            Server IP 🌍
            >
            Handshaking with 192.168.1.9 on port 1337...
            Connection established!
            Nickname 👾
            >
            Lobby Size 📦
            >
            Joining the lobby...⛩
            You have correctly joined the lobby!
            > Wait for the match startup...|
        */
        System.out.print(ANSI_GREEN+SUCCESS_LOBBY_ACCESS+NEW_LINE);
        System.out.print(COLORMODE+CLI_INPUT+WAITSTART+NEW_LINE);
        rowCounter=11;
    }

}
