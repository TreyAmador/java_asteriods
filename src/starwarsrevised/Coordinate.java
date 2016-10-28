package starwarsrevised;

import java.util.Random;

public class Coordinate {
    public static int MAX_Y = 600;
    public static int MAX_X = 800;
    public static int INTERVAL = 20;
    private int m_X, m_Y;
    private Random rand;
    public Coordinate() {
        rand = new Random();
        setX(rand.nextInt(MAX_X) + 1);
        setY(rand.nextInt(MAX_Y) + 1);        
    }
    
    public int getX() {return m_X;}
    public int getY() {return m_Y;}
    public void setX(int X) {m_X = X;}
    public void setY(int Y) {m_Y = Y;}
    public void moveDown() {
        if(m_Y >= MAX_Y) { // If we reached the bottom of the screen, reset to top
            m_Y = 0;
            m_X = rand.nextInt(MAX_X) + 1;
        }            
        else
            m_Y++;
    }
}
