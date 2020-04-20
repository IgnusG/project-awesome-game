/*
 *  Autor: Jonatan Juhas - Informatik WS15
 *  Projekt: Objects
 */

package ObjectLibrary;

import java.util.Hashtable;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
// Takes care of all Moving Objects
public class Entity extends Object implements Runnable {
    
    private final static AtomicInteger nextId = new AtomicInteger(0);
    public final int id;
    
    private int positionX;
    private int positionY;
    
    private boolean lastLocked = false;
    private int lastPositionX;
    private int lastPositionY;
    
    private int lastKnownPositionX;
    private int lastKnownPositionY;
    
    protected final World world;
    
    private Thread me;
    
    public int[] getPosition(){
        int[] result = {positionX,positionY};
        return result;
    }
    public int getPositionX(){
        return getPosition()[0];
    }
    public int getPositionY(){
        return getPosition()[1];
    }
    
    public Entity(Objects type, int positionX, int positionY, World world) {
        super(type);
        this.positionX = positionX;
        this.positionY = positionY;
        this.world = world;
        
        id = nextId.get();
        nextId.addAndGet(1);
    }
    
    public void createThread(){
        me = new Thread(this);
    }
    public Thread getThread(){
        return me;
    }
    
    public void lockLastPosition(){
        lastLocked = true;
    }
    public void unlockLastPosition(){
        lastLocked = false;
        lastPositionX = lastKnownPositionX;
        lastPositionY = lastKnownPositionY;
    }
    
    public boolean isBacktracking(){
        return (positionX == lastPositionX && positionY == lastPositionY);
    }
    
    public boolean isAt(int positionX, int positionY){
        return (this.positionX == positionX && this.positionY == positionY);
    }
    
    public boolean moveTo(int positionX, int positionY){
        if(world.canMoveTo(positionX, positionY)){
            
            if(!lastLocked){
                lastPositionY = positionY;
                lastPositionX = positionX;
            }
            
            lastKnownPositionY = positionY;
            lastKnownPositionX = positionX;
            
            this.positionX = positionX;
            this.positionY = positionY;
            
            return true;
        }
        return false;
    }
    
    public void reverse(){
        positionX = lastKnownPositionX;
        positionY = lastKnownPositionY;
    }
    
    public boolean moveUp(){
        if(world.canMoveTo(positionX, positionY-1)){
            moveTo(positionX,positionY-1);
            return true;
        }
        return false;
    }
    public boolean moveDown(){
        if(world.canMoveTo(positionX, positionY+1)){
            moveTo(positionX,positionY+1);
            return true;
        }
        return false;
    }
    public boolean moveLeft(){
        if(world.canMoveTo(positionX-1, positionY)){
            moveTo(positionX-1,positionY);
            return true;
        }
        return false;
    }
    public boolean moveRight(){
        if(world.canMoveTo(positionX+1, positionY)){
            moveTo(positionX+1,positionY);
            return true;
        }
        return false;
    }
    
    public boolean inVicinity(Entity entity, int limit){
        return (Math.abs(entity.getPositionX()-positionX)<limit)&&(Math.abs(entity.getPositionY()-positionY)<limit);
    }
    
    public void moveCompletelyRandom() {
        Random random = new Random();
loop:   while (true) {
            switch (random.nextInt(3)) {
                case 0:
                    if (moveUp())
                        break loop;
                case 1:
                    if (moveRight())
                        break loop;
                case 2:
                    if (moveDown())
                        break loop;
                case 3:
                    if (moveLeft())
                        break loop;
            }
        }
    }
    
    public boolean moveTowards(Entity entity, int limit){
        
        if(!inVicinity(entity,limit))
            return false;
        
        int offX = entity.getPositionX()-positionX;
        int offY= entity.getPositionY()-positionY;
        
        if(offY < 0){
            if(!moveUp())
                if(offX < 0)
                    return moveLeft();
                else
                    return moveRight();
            return true;
        } else if (offY > 0){
            if(!moveDown())
                if (offX < 0)
                    return moveLeft();
                else
                    return moveRight();
            return true;
        } else {
            if (offX < 0) {
                return moveLeft();
            } else if (offX > 0) {
                return moveRight();
            } else {
                moveCompletelyRandom();
                return true;
            }
        }
    }
    
    public boolean moveAwayFrom(Entity entity, int limit) {

        int offX = entity.getPositionX() - positionX;
        int offY = entity.getPositionY() - positionY;

        if (offY < 0) {
            if (!moveDown())
                if (offX < 0)
                    return moveRight();
                else
                    return moveLeft();
            return true;
        } else if (offY > 0) {
            if (!moveUp())
                if (offX < 0)
                    return moveRight();
                else
                    return moveLeft();
            return true;
        } else {
            if (offX < 0)
                return moveRight();
            else
                return moveLeft();
        }
    }
    
    public static Hashtable readAttributes(String attr){
        Hashtable<String,String> hash = new Hashtable();
        String[] string = attr.split("\\?");
        hash.put("name", string[0]);
        
        String[] string2;
        
        //if(string[1].contains(";"))
            string2 = string[1].split(";");
        /*else{
            string2 = new String[1];
            string2[0] = string[1];
        }*/
        
        for(String s : string2){
            String[] sDivided = s.split(":");
            hash.put(sDivided[0],(sDivided.length == 2) ? sDivided[1] : "");
        }
        
        return hash;
    }
    
    public String encode(){
        return instanceOfPadded()+"position:"+positionX+","+positionY;
    }
    
    public String instanceOfPadded(){
        switch(super.is()){
            case ENEMY: return 4+"?";
            case PLAYER: return "";
            default: return -1+"?";
        }
    }
    
    public static Entity loadEntity(String entityString, World world){
        Hashtable<String, String> hash = Entity.readAttributes(entityString);

        // General modifyieble code goes here
        
        int type = Integer.parseInt(hash.get("name"));
        
        String[] positionString = hash.get("position").split(",");
        int[] position = {Integer.parseInt(positionString[0]), Integer.parseInt(positionString[1])};

        return Object.createEntity(type, position[0], position[1], world);
    }

    @Override
    public void run() {
        System.out.println("Entity "+this+" does not override the generic run Method. Thread Died...");
    }
    
    @Override
    public String toString() {
        return "#" + id;
    }
}
