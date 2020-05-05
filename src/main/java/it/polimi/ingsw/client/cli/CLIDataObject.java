package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.clientModel.basic.Block;
import it.polimi.ingsw.client.clientModel.basic.Color;
import it.polimi.ingsw.client.network.data.dataInterfaces.CellInterface;

import java.util.HashMap;

public class CLIDataObject {

    //------------------ # ANSI Colors 256 bit # ------------------
    private static final String CODE_BLUE = "33";
    private static final String CODE_LIGHTBLUE = "75";
    private static final String CODE_GRAY = "248";
    private static final String CODE_GREEN = "41";
    private static final String CODE_BROWN = "130";
    private static final String CODE_WHITE = "255";
    private static final String ANSI_PREFIX = "\u001b[38;5;";

    private static final String ANSI_BLUE = ANSI_PREFIX+CODE_BLUE+"m";
    private static final String ANSI_LIGHTBLUE = ANSI_PREFIX+CODE_LIGHTBLUE+"m";
    private static final String ANSI_GRAY = ANSI_PREFIX+CODE_GRAY+"m";
    private static final String ANSI_GREEN = ANSI_PREFIX+CODE_GREEN+"m";
    private static final String ANSI_BROWN = ANSI_PREFIX+CODE_BROWN+"m";
    private static final String ANSI_WHITE = ANSI_PREFIX+CODE_WHITE+"m";

    //------------------ # Game Objects # ------------------
    protected static final String WORKER = "✲";
    protected static final String DOME = ANSI_LIGHTBLUE+"◉"+ANSI_WHITE;
    protected static final String GRASS = ANSI_GREEN+"᭟"+ANSI_WHITE;

    //Data
    private static final String cellDataTemplate = "%s %s";
    private static final String freeCellTemplate = "  "+GRASS+ANSI_WHITE;
    private String[] cellsData; // ┃col1┃col2┃col3┃col4┃col5┃
    private final HashMap<Block,String> towersInformation;
    private final HashMap<Color,String> colorsInformation;

    /**
     * Class Constructor
     */
    public CLIDataObject() {
        this.cellsData = new String[5];
        this.towersInformation = new HashMap<>();
        this.colorsInformation = new HashMap<>();
        for(int i=0;i<5;i++){
            String data = new String(freeCellTemplate);
            cellsData[i]=data;
        }
        towersInformation.put(Block.GROUND,GRASS);
        towersInformation.put(Block.LEVEL_1,ANSI_WHITE+"1");
        towersInformation.put(Block.LEVEL_2,ANSI_WHITE+"2");
        towersInformation.put(Block.LEVEL_3,ANSI_WHITE+"3");
        towersInformation.put(Block.DOME,DOME);
        colorsInformation.put(Color.BLUE,ANSI_BLUE+WORKER+ANSI_WHITE);
        colorsInformation.put(Color.BROWN,ANSI_BROWN+WORKER+ANSI_WHITE);
        colorsInformation.put(Color.GREY,ANSI_GRAY+WORKER+ANSI_WHITE);
    }

    //DONE: Methods

    /**
     * Reads a Cell and writes her content in a string
     * @param col is the cell column coordinate
     * @param cellInterface is the cell
     */
    public void writeCellData(int col, CellInterface cellInterface){
        String cellWorker;
        if(cellInterface.getPlayer()!=null)
            cellWorker= colorsInformation.get(cellInterface.getWorkerColor());
        else
            cellWorker=" ";
        String cellTower = towersInformation.get(cellInterface.getLastBlock());
        cellsData[col]=String.format(cellDataTemplate,cellWorker,cellTower);
    }

    //GETTER and SETTERS
    public String getCellContent(int col){ return cellsData[col]; }

}
