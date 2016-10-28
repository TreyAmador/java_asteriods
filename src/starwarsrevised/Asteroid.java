package starwarsrevised;

public class Asteroid extends Coordinate{
    
    private boolean asteroid_Visible = true;
    //private Coordinate m_Coordinate;
    private int astX, astY;
    
    public void setAstX(int X) {astX = X;}
    public void setAstY(int Y) {astY = Y;}
    
    public int getAstX() {return astX;}
    public int getAstY() {return astY;}
    
    public boolean getVisible(){
        
        return asteroid_Visible;
    }
    
    public void explodeAsteroid(boolean asteroidVisible){
        asteroid_Visible = asteroidVisible;
    }
    
    public void moveAstDown(int speed){
        astY += speed;
    }
    
}