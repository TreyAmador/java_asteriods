package starwarsrevised;

public class Laser extends Coordinate {
    private boolean m_Visible;
    private Coordinate m_FalconCoordinates;
    public void setVisible(boolean Visible) 
    {
        m_Visible = Visible;
        if(m_Visible) {
            setX(m_FalconCoordinates.getX() + 49);
            setY(m_FalconCoordinates.getY() + 8);
        }
    }
    public boolean getVisible() {return m_Visible;}
    public void moveUp() {
        if(getY() > 0)
            setY(getY() - 10);
        else
            setVisible(false);
    }
    public Laser(Coordinate FalconCoordinates) {
        super();
        m_FalconCoordinates = FalconCoordinates;
        setVisible(false);
    }
}
