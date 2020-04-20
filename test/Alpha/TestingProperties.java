package Alpha;

/*
 *  Autor: Jonatan Juhas - Informatik WS14
 *  Projekt:
 */



// Reading files in JAVA

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

public class TestingProperties {
    public static void printTable(){}
    
    public static void main(String[] args) throws IOException {
        FileInputStream in = null;
        Properties props = new Properties();
        Set test;
        int[][] world;
        
        try {
            in = new FileInputStream("level_small.properties");
            props.load(in);
            
            world = new int[10][10];
            
            for(int i = 0; i < 10; i++)
                for(int k = 0; k < 10; k++)
                    world[i][k] = -1;
            
            // Parsing properties file
            test = props.keySet();
            for(Object s : test){
                String[] coords = ((String) s).split(",");
                if(coords.length == 2)
                    world[Integer.parseInt(coords[0])][Integer.parseInt(coords[1])] = Integer.parseInt(props.getProperty((String) s));
            }
            
            System.out.println(props.getProperty("41"));
            
            for(int[] row : world){
                for(int column : row){
                    switch(column){
                        case -1: System.out.print(" ");
                            break;
                        case 0: System.out.print("X");
                            break;
                        case 1: System.out.print("E");
                            break;
                        case 2: System.out.print("F");
                            break;
                        case 3: System.out.print("S");
                            break;
                        case 4: System.out.print("D");
                            break;
                        case 5: System.out.print("K");
                    }
                }
                System.out.println();
            }
                
            System.out.println("Done!");
        } finally {
            if(in != null)
                in.close();
        }
    }
}
