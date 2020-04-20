/*
 *  Autor: Jonatan Juhas - Informatik WS15
 *  Projekt: Objects
 */

package ObjectLibrary.Objects;

import ObjectLibrary.Entity;
import ObjectLibrary.World;
import ObjectLibrary.Interfaces.canRender;
import com.googlecode.lanterna.terminal.Terminal.Color;
import java.util.Random;
import DisplayDriver.SpecialChar;

public class Enemy extends Entity implements Runnable, canRender {
    
    private static SpecialChar style = new SpecialChar('\u2603',Color.RED,Color.DEFAULT);
    
    public static SpecialChar showOf() {
        return style;
    }
    
    private final Random random = new Random();
    
    public enum EnemyType{
        RANDOM,CHASE_PLAYER_10,CHASE_PLAYER_5, RUN_AWAY
    }
    
    EnemyType type;
    
    public Enemy(int positionX, int positionY, World world) {
        super(Objects.ENEMY, positionX, positionY, world);
        
        EnemyType[] types = EnemyType.values();
        
        int randomInt = random.nextInt(types.length);
        type = EnemyType.CHASE_PLAYER_10;//types[randomInt];
    }
    public Enemy(EnemyType type, int positionX, int positionY, World world) {
        super(Objects.ENEMY, positionX, positionY, world);
        
        this.type = type;
    }
    
    @Override
    public boolean moveTo(int positionX, int positionY){
        if(super.moveTo(positionX, positionY)){
            Player player = world.getPlayer();
            if (player.isAt(positionX, positionY))
                player.hit();
            return true;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "Enemy ["+super.toString()+"]";
    }
    
    public void randomWait() throws InterruptedException {
        if(random.nextDouble() < 0.2){
            Thread.sleep(300);
        }
    }

    @Override
    public void run() {
        
        System.out.println(this+": Thread started");
        try {
            while(true){
                if(world.allowAI(this)){
                    
                    randomWait();

                    switch(type){
                        case RANDOM:
                            moveCompletelyRandom();
                            break;
                        case CHASE_PLAYER_10:
                            if(!moveTowards(world.getPlayer(),40))
                                moveCompletelyRandom();
                            break;
                        case CHASE_PLAYER_5:
                            if(!moveTowards(world.getPlayer(),30))
                                moveCompletelyRandom();
                            break;
                        case RUN_AWAY:
                            if(!moveAwayFrom(world.getPlayer(),20))
                                moveCompletelyRandom();
                            break;
                    }
                }
                    Thread.sleep(world.enemySpeed);
            }
        } catch (InterruptedException ex) {
            System.out.println("Entity ("+this+"): Thread terminated");
            return;
        }
    }
    
    @Override
    public SpecialChar display(){
        return style;
    }
}
