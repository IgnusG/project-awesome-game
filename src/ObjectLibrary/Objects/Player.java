/*
 *  Autor: Jonatan Juhas - Informatik WS15
 *  Projekt: Objects
 */

package ObjectLibrary.Objects;

import ObjectLibrary.Entity;
import ObjectLibrary.World;
import ObjectLibrary.Interfaces.canRender;
import java.util.Hashtable;
import java.util.Properties;
import DisplayDriver.SpecialChar;
import com.googlecode.lanterna.terminal.Terminal.Color;

public class Player extends Entity implements canRender {
    private String name;
    private static SpecialChar style = new SpecialChar('\u263A',Color.GREEN,Color.DEFAULT);
    
    public static SpecialChar showOf(){
        return style;
    }
    
    private final int maxLives = 9;
    private int lives = maxLives;
    
    private int score;
    private boolean completed = false;
    
    private int bullets = 0;
    private final int maxBullets = 10;
    
    private int bombs = 0;
    private final int maxBombs = 3;
    
    private int keys = 0;

    public Player(String name, int positionX, int positionY, World world) {
        super(Objects.PLAYER, positionX, positionY, world);
        this.name = name;
    }
    
    private Player(String name,int lives,int score, int bullets, int bombs, int keys, int[] position, World world){
        super(Objects.PLAYER, position[0],position[1], world);
        this.name = name;
        this.lives = lives;
        this.score = score;
        this.bullets = bullets;
        this.bombs = bombs;
        this.keys = keys;
    }
    
    public void exitMap(int status){
        switch(status){
            case 0: world.halt();
                break;
            case 1: completed = true;
                break;
            default: assert false;
        }
    }
    
    @Override
    public boolean moveTo(int positionX, int positionY){
        if(super.moveTo(positionX, positionY)){
            switch (world.map[positionX][positionY].is()) {
                case KEY:
                    acquireKey((Key) world.map[positionX][positionY]);
                    world.map[positionX][positionY] = new Space();
                    break;
                case ENEMY:
                case TRAP:
                    hit();
                    break;
                case EXIT:
                    if (hasAllKeys()) {
                        exitMap(1);
                    }
                    break;
            }
            return true;
        }
        return false;
    }
    
    @Override
    public String toString(){
        return "Player "+name;
    }
    
    @Override
    public String encode(){
        return name+"?lives:"+lives+";score:"+score+";bullets:"+bullets+";bombs:"+bombs+";keys:"+keys+";"+super.encode();
    }
    
    public String repeat(char c, int times){
        String out = "";
        for(int i = 0; i < times; i++)
            out+= c;
        return out;
    }
    
    public static Player loadPlayer(Properties props, World world){
        Hashtable<String, String> hash = Entity.readAttributes(props.getProperty("player"));
        
        int lives = Integer.parseInt(hash.get("lives"));
        int score = Integer.parseInt(hash.get("score"));
        int bullets = Integer.parseInt(hash.get("bullets"));
        int bombs = Integer.parseInt(hash.get("bombs"));
        int keys = Integer.parseInt(hash.get("keys"));
        
        String[] positionString = hash.get("position").split(",");
        int[] position = {Integer.parseInt(positionString[0]),Integer.parseInt(positionString[1])};
        
        return new Player(hash.get("name"),lives,score,bullets,bombs,keys, position, world);
    }
    
    public void hit(){
        hit(1);
    }
    public void hit(int damage){
        if(lives > 0)
            lives--;

        score -= 160;
    }
    
    public boolean hasWon(){
        return completed;
    }
    public boolean hasDied(){
        return lives == 0;
    }
    public void acquireKey(Key key){
        keys++;
        
        int normalScore = Math.min(keys, world.getKeyCount())*200;
        int uberScore = keys - world.getKeyCount();
        uberScore = (uberScore < 0) ? 0 : uberScore * 1000;
        
        score = normalScore+uberScore;
    }
    public boolean hasAllKeys(){
        if(keys >= world.getKeyCount())
            return true;
        return false;
    }
    
    public String getLives(){
        return repeat('\u2665',lives);
    }
    public String getName(){
        return name;
    }
    public int getAcquiredKeys(){
        return keys;
    }
    public int getScore(){
        return score;
    }
    public void activateScore(int score){
        this.score = score;
    }
    
    @Override
    public SpecialChar display(){
        return style;
    }
}
