/*
 *  Autor: Jonatan Juhas - Informatik WS14
 *  Projekt: Objects
 */

package ObjectLibrary;

import ObjectLibrary.Interfaces.canRender;
import ObjectLibrary.Exceptions.InvalidFileException;
import ObjectLibrary.Exceptions.InvalidSaveException;
import ObjectLibrary.Object.Objects;
import ObjectLibrary.Objects.Wall;
import ObjectLibrary.Objects.Space;
import ObjectLibrary.Objects.Player;
import com.googlecode.lanterna.terminal.Terminal;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

// Speichert die ganze Level Karte (weiter nur World gennant)
public class World {
    
    public final String name;
    public final String type;
    public final int level;
    public final int enemySpeed;
    
    public final int width;
    public final int height;
    
    private int renderingStartX = 0;
    private int renderingEndX = 0;
    private int renderingStartY = 0;
    private int renderingEndY = 0;
    
    private int keyCount; 
    
    private boolean exitWorld = false;
    
    public final canRender[][] map;
    public final ArrayList<Entity> dynamicObjectList = new ArrayList<>();
    
    public World(Properties props) throws InvalidSaveException {
        if(!props.containsKey("isValidSave"))
            throw new InvalidSaveException();
        if(props.getProperty("isValidSave").equals("true")){
            name = props.getProperty("worldname");
            type = props.getProperty("type");
            level = Integer.parseInt(props.getProperty("level"));
            enemySpeed = Integer.parseInt(props.getProperty("enemySpeed"));
            
            height = Integer.parseInt(props.getProperty("height"));
            width = Integer.parseInt(props.getProperty("width"));
            
            map = new canRender[width][height];
            
            dynamicObjectList.add(Player.loadPlayer(props, this));
            
            int counter = 0;
            String entityString = props.getProperty("entity#"+counter);
            while(entityString != null){
                dynamicObjectList.add(Entity.loadEntity(entityString, this));
                counter++;
                entityString = props.getProperty("entity#"+counter);
            }
            
            for (int i = 0; i < width; i++) {
                for (int k = 0; k < height; k++) {
                    try {
                        int definedObject = Integer.parseInt(props.getProperty(i + "," + k));
                        Item item = Object.createItem(definedObject);
                        if (item != null) {
                            map[i][k] = item;
                        } else {
                            dynamicObjectList.add(Object.createEntity(definedObject,i,k,this));
                        }
                    } catch (NumberFormatException error) {
                        map[i][k] = new Space();
                    }
                }
            }
            
            try{
                keyCount = Integer.parseInt(props.getProperty("keyCount"));
            } catch (NumberFormatException err){
                assert false;
            }
            createWallDesign();
        } else {
            throw new InvalidSaveException();
        }
    }
    
    public World(Properties props, String playerName, int level) throws InvalidFileException {
        
        this.name = "Level "+level;
        if(!props.containsKey("type"))
            type = "No Info";
        else
            type = props.getProperty("type");
        
        this.level = level;
        
        if(type.endsWith("small"))
            enemySpeed = 400;
        else
            enemySpeed = 200;
        
        try {
            height = Integer.parseInt(props.getProperty("Height"));
            width = Integer.parseInt(props.getProperty("Width"));
        } catch (NumberFormatException error){
            throw new InvalidFileException();
        }
        
        map = new canRender[width][height];
        
        for(int k = 0; k < width; k++){
            for(int i = 0; i < height; i++){
                try {
                    int definedObject = Integer.parseInt(props.getProperty(k + "," + i));
                    Item item = Object.createItem(definedObject);
                    if (item != null) {
                        map[k][i] = item;
                        if (item.is(Objects.ENTRY))
                            dynamicObjectList.add(new Player(playerName, k, i, this));
                        if (item.is(Objects.KEY))
                            keyCount++;
                    } else {
                        dynamicObjectList.add(Object.createEntity(definedObject, k, i, this));
                        map[k][i] = new Space();
                    }
                } catch (NumberFormatException error) {
                    map[k][i] = new Space();
                }
            }
        }
        Random random = new Random();
        System.out.println(this+" has been created: Height "+height+" Width"+width);
        keyCount = 1+((keyCount < 3)?random.nextInt(keyCount):random.nextInt(3));
        
        createWallDesign();
    }
    
    public void setRenderingStart(int positionX, int positionY){
        renderingStartX = positionX;
        renderingStartY = positionY;
    }
    public void setRenderingEnd(int positionX, int positionY){
        renderingEndX = positionX;
        renderingEndY = positionY;
    }
    
    public int getKeyCount(){
        return keyCount;
    }
    
    // Vereinfachte Methode für das Properties lesen
    public static Properties readProperties(String name){
        File in = new File(name+".properties");
        return readProperties(in);
    }
    
    public static Properties readProperties(File file){
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            Properties props = new Properties();
            in = new FileInputStream(file);
            props.load(in);
            return props;
        } catch (IOException ex) {
            return null;
        } finally {
            try {
                in.close();
            } catch (IOException ex) {}
        }
    }
    
    private boolean isTopWall(int positionX, int positionY){
        
        /*if(positionY == 0)
            if(!isLeftWall(positionX,positionY, false) && !isRightWall(positionX,positionY, false))
                return true;
        */
        // Nullpointer protection
        if(positionX >= width || positionY >= height || positionX < 0 || positionY < 1)
            return false;
        
        return map[positionX][positionY-1] instanceof Wall;
    }
    
    private boolean isBottomWall(int positionX, int positionY){
        /*
        if(positionY == height-1)
            if (!isLeftWall(positionX, positionY, false) && !isRightWall(positionX, positionY, false))
                return true;
        */   
        // Nullpointer protection
        if(positionX >= width || positionY >= height-1 || positionX < 0 || positionY < 0)
            return false;
        
        return map[positionX][positionY+1] instanceof Wall;
    }
    
    private boolean isLeftWall(int positionX, int positionY){
        return isLeftWall(positionX, positionY, true);
    }
    private boolean isLeftWall(int positionX, int positionY, boolean compare){
        /*
        if(positionX == 0 && compare)
            if (!isTopWall(positionX, positionY) && !isBottomWall(positionX, positionY)) 
                return true;
        */    
        // Nullpointer protection
        if(positionX >= width || positionY >= height || positionX < 1 || positionY < 0)
            return false;
        
        return map[positionX-1][positionY] instanceof Wall;
    }
    
    private boolean isRightWall(int positionX, int positionY){
        return isRightWall(positionX, positionY, true);
    }
    private boolean isRightWall(int positionX, int positionY, boolean compare){
        /*
        if(positionX == width-1 && compare)
            if (!isTopWall(positionX, positionY) && !isBottomWall(positionX, positionY)) 
                return true;
        */
        // Nullpointer protection
        if(positionX >= width-1 || positionY >= height || positionX < 0 || positionY < 0)
            return false;
        
        return map[positionX+1][positionY] instanceof Wall;
    }
    
    private void createWallDesign(){
        for(int row = 0; row < height; row++)
            for(int column = 0; column < width; column++) {
                Object object = (Object) map[column][row];
                if(object instanceof Wall){
                    int total = 0;
                    if(isTopWall(column,row))
                        total += 1;
                    if(isRightWall(column,row))
                        total += 10;
                    if(isBottomWall(column,row))
                        total += 20;
                    if(isLeftWall(column,row))
                        total += 60;
                    
                    char c = ' ';
                    
                    // Without exiting Walls
                    switch(total){
                        case 0: c = '\u256C';
                            break;
                        case 11: c = '\u255A';
                            break;
                        case 1:
                        case 20:
                        case 21:
                            c = '\u2551';
                            break;
                        case 30:
                            c = '\u2554';
                            break;
                        case 31:
                            c = '\u2560';
                            break;
                        case 61:
                            c = '\u255D';
                            break;
                        case 10:
                        case 60:
                        case 70:
                            c = '\u2550';
                            break;
                        case 71:
                            c = '\u2569';
                            break;
                        case 80:
                            c = '\u2557';
                            break;
                        case 81:
                            c = '\u2563';
                            break;
                        case 90:
                            c = '\u2566';
                            break;
                        case 91:
                            c = '\u256C';
                            break;
                    }
                    
                    object.applyStyle(c,Terminal.Color.DEFAULT,Terminal.Color.DEFAULT);
                }
            }
    }
    
    public boolean allowAI(Entity entity){
        boolean isPastStart = (renderingStartX < entity.getPositionX()+3) && (renderingStartY < entity.getPositionY()+3);
        boolean isBeforeEnd = (renderingEndX > entity.getPositionX()-3) && (renderingEndY > entity.getPositionY()-3);
        return (isPastStart&&isBeforeEnd); 
    }
    
    public void halt(){
        exitWorld = true;
    }
    public boolean halted(){
        return exitWorld;
    }
    
    // Speichert das Spiel
    public static boolean saveGame(World world){
        String filename = "Not Defined";
        try {
            Properties props = new Properties();
            
            props.setProperty("isValidSave","true");
            
            DateFormat dateFormat = new SimpleDateFormat("HH:mm dd.MM.yyyy");
            Date date = new Date();
            
            props.setProperty("saveinfo", world.name+" : "+world.type+" : "+dateFormat.format(date));
            props.setProperty("worldname", world.name);
            props.setProperty("type", world.type);
            props.setProperty("level", world.level+"");
            props.setProperty("keyCount", world.getKeyCount()+"");
            
            props.setProperty("width", ""+world.width);
            props.setProperty("height", ""+world.height);
            
            props.setProperty("player", world.getPlayer().encode());
            
            props.setProperty("enemySpeed", world.enemySpeed+"");
            int counter = 0;
            for(Entity entity : world.dynamicObjectList){
                if(!entity.is(Objects.PLAYER)){
                    props.setProperty("entity#"+counter,entity.encode());
                    counter++;
                }
            }
            
            for (int i = 0; i < world.width; i++) {
                for (int k = 0; k < world.height; k++) {
                    if(!world.map[i][k].is(Objects.SPACE))
                        props.setProperty(i+","+k, ""+((Object) world.map[i][k]).instanceOf());
                }
            }
            
            File file;
            
            counter = 1;
            while(true){
                file = new File("AwsmGameSave-"+counter+".properties");
                if(file.exists())
                    counter++;
                else
                    break;
            }
            FileOutputStream out = new FileOutputStream(file);
            
            props.store(out,"AwsmGame - Jonatan Juhas Production, a Capstone Project in cooperation with InfinityLines");
            
            return true;
        } catch (IOException ex) {
            System.err.println("Game File "+filename+" encountered a I/O Exception");
            return false;
        }
    }
    
    public Player getPlayer(){
        for(Entity entity : dynamicObjectList){
            if(entity instanceof Player)
                return (Player) entity;
        }
        return null;
    }
    
    public boolean canMoveTo(int positionX, int positionY){
        if(positionX < 0 || positionX >= width || positionY < 0 || positionY >= height)
            return false;
        return !(map[positionX][positionY].is(Objects.WALL));
    }
    
    @Override
    public String toString(){
        return name;
    }
}
