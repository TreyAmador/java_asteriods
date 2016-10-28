package starwarsrevised;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.event.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
//import java.util.HashSet;
//import java.util.Set;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class MainPanel extends JPanel {   

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String FALCON_FILE = "C:\\Users\\Trey\\Documents\\Java Workspace\\java_asteroids\\resources\\spiked_ship.PNG";
    public static final String ASTEROID_FILE = "C:\\Users\\Trey\\Documents\\Java Workspace\\java_asteroids\\resources\\spinning_asteroid_1.gif";
    public static final String EXPLOSION_FILE = "C:\\Users\\Trey\\Documents\\Java Workspace\\java_asteroids\\resources\\spinning_asteroid_exp.jpg";
    
    public static final String SOUNDTRACK = "C:\\Users\\Trey\\Documents\\Java Workspace\\java_asteroids\\resources\\gybemoya.mid";
    public static final String EXPLOSION_FX = "C:\\Users\\Trey\\Documents\\Java Workspace\\java_asteroids\\resources\\Depth Charge 2-SoundBible.com-338644910.wav";
    public static final String CRASH_FX = "C:\\Users\\Trey\\Documents\\Java Workspace\\java_asteroids\\resources\\Flashbang-Kibblesbob-899170896.wav";
    public static final String LASER_FX = "C:\\Users\\Trey\\Documents\\Java Workspace\\java_asteroids\\resources\\Pulse-gun-01.wav";
    //public static final String EXPLOSION_FILE = "C:\\Users\\Trey\\Documents\\Java Workspace\\java_asteroids\\resources\\spinning_asteroid_1.gif";
    
    private BufferedImage m_Falcon, m_Asteroid, m_AsteroidExplode;
    public static int MAX_STARS = 50;
    public static int MAX_LASERS = 3;
    public static int MAX_ASTEROIDS = 66;
    public static final int ARBITRARY_FACTOR = 2;
    private int score, speedIncrease, destroyed, currentAsteroid, current_asteroid_Y;
    
    private Coordinate m_Star[], m_FalconCoordinates;
    private Asteroid[] m_AsteroidCoordinates;
    private Asteroid m_AsteroidExplodeCoordinates; //new stuff
    private Laser m_Lasers [];
    private Timer m_StarTimer;
    private Rectangle rectAsteroid, rectLaser, rectFalcon;
    private boolean game_over_message = true;
    private boolean explosionBoolean = false;
    
    private AudioInputStream explosionFX, laserFX, crashFX, soundtrackStream;
    private Clip clipExplode, clipLaser, clipCrash, clipSoundtrack;
    
    private int[][] asteroidPosition = {
        {225, -150}, {330, -560}, {550, -520}, {50, -1010}, {235, -1053}, {366, -1038},
        {80, -1538}, {222, -1438}, {250, -1518}, {366, -1500,}, {150, -2050}, {300, -2060}, 
        {320, -2190}, {400, -2110}, {500, -2813}, {466, -3138}, {300, -3338}, {222, -3408}, 
        {441, -3538}, {250, -3498,}, {700, -3701}, {50, -3910}, {235, -4253}, {466, -5338}, 
        {80, -5538}, {222, -5438}, {250, -5518}, {366, -5500,}, {50, -5810}, {235, -6653}, 
        {466, -6738}, {80, -6838}, {222, -6938}, {300, -7218}, {366, -7700,}, {100, -7950}, 
        {80, -8038}, {222, -8138}, {290, -8018}, {366, -9000,}, {150, -9050}, {408, -9060}, 
        {320, -9090}, {360, -10010}, {500, -14013}, {80, -14538}, {202, -14438}, {250, -14518}, 
        {366, -14500,}, {150, -15050}, {300, -16060}, {520, -16090}, {400, -17010}, {50, -17813}, 
        {166, -18638}, {200, -18050}, {300, -19060}, {340, -20090}, {388, -20010}, {510, -20813}, 
        {429, -21638}, {167, -22050}, {288, -22060}, {460, -22090}, {99, -22010}, {289, -22813},
    };
    
    /**
     * Creates new form MainPanel
     */
    public MainPanel() {
        
        score = 0;
        speedIncrease = 1;
        destroyed = 0;
        currentAsteroid = 0;
        current_asteroid_Y = 0;
        
        try {
            m_Falcon = ImageIO.read(new File(FALCON_FILE));
            m_FalconCoordinates = new Coordinate();
            m_FalconCoordinates.setX(((Coordinate.MAX_X) / 2) - 25);
            m_FalconCoordinates.setY(Coordinate.MAX_Y - 125);
            
            m_AsteroidCoordinates = new Asteroid[MAX_ASTEROIDS];

            for(int i = 0; i < MAX_ASTEROIDS; i++){
                m_Asteroid = ImageIO.read(new File(ASTEROID_FILE));
                m_AsteroidCoordinates[i] = new Asteroid();   //?
                
                m_AsteroidCoordinates[i].setAstX(asteroidPosition[i][0]);
                m_AsteroidCoordinates[i].setAstY(asteroidPosition[i][1]);
            }
            
            m_AsteroidExplode = ImageIO.read(new File (EXPLOSION_FILE));
            m_AsteroidExplodeCoordinates = new Asteroid(); //new stuff
            
            explosionFX = AudioSystem.getAudioInputStream(new File(EXPLOSION_FX));
            clipExplode = AudioSystem.getClip();
            clipExplode.open(explosionFX);
            
            laserFX = AudioSystem.getAudioInputStream(new File(LASER_FX));
            clipLaser = AudioSystem.getClip();
            clipLaser.open(laserFX);
            
            crashFX = AudioSystem.getAudioInputStream(new File(CRASH_FX));
            clipCrash = AudioSystem.getClip();
            clipCrash.open(crashFX);
            
            soundtrackStream = AudioSystem.getAudioInputStream(new File(SOUNDTRACK));
            clipSoundtrack = AudioSystem.getClip();
            clipSoundtrack.open(soundtrackStream);
            clipSoundtrack.start();
            
            setFocusable(true);
            initStars();            
            this.addKeyListener(new FalconHandler());
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(this, "Unable to load input file");
        }
        initComponents();        
    }
    
    private void paintStars(Graphics g) {
        g.setColor(Color.cyan);
        for(int i = 0; i < MAX_STARS; i++) {
            g.drawRect(m_Star[i].getX(), m_Star[i].getY(), 1, 1);
        }
        g.setColor(Color.CYAN);
        for(int i = 0; i < MAX_LASERS; i++) {
            if(m_Lasers [i].getVisible()) {
                g.drawLine(m_Lasers[i].getX() - 1, m_Lasers[i].getY(), m_Lasers[i].getX() - 1, m_Lasers[i].getY() - 15);
                g.drawLine(m_Lasers[i].getX() + 1, m_Lasers[i].getY(), m_Lasers[i].getX() + 1, m_Lasers[i].getY() - 15);
                g.drawLine(m_Lasers[i].getX(), m_Lasers[i].getY(), m_Lasers[i].getX(), m_Lasers[i].getY() - 15);
                m_Lasers [i].moveUp();
            }
        }
    }
    /**
     * Creates 50 stars randomly to display in the screen
     */
    private void initStars() {
        m_Star = new Coordinate[MAX_STARS];
        for(int i = 0; i < MAX_STARS; i++)
            m_Star[i] = new Coordinate();
        m_Lasers = new Laser[MAX_LASERS];
        for(int i = 0; i < MAX_LASERS; i++) 
            m_Lasers[i] = new Laser(m_FalconCoordinates);
        
        m_StarTimer = new Timer(Coordinate.INTERVAL, new starTimerHandler());
        m_StarTimer.start();
    }
    
    // Method called by timer to move the stars
    private void moveStars() {
        for(int i = 0; i < MAX_STARS; i++)
            m_Star[i].moveDown();
        
    }
    // Overrides paint method
    public void paint(Graphics g) {
        g.setColor(Color.BLACK);        
        g.fillRect(0, 0, Coordinate.MAX_X, Coordinate.MAX_Y);
        paintStars(g);
        g.drawImage(m_Falcon, m_FalconCoordinates.getX(), m_FalconCoordinates.getY(), this);
        
        for(int i = 0; i < MAX_ASTEROIDS; i++){
            if(m_AsteroidCoordinates[i].getVisible()){
                g.drawImage(m_Asteroid, m_AsteroidCoordinates[i].getAstX(), m_AsteroidCoordinates[i].getAstY(), 100, 100, this); //?
            }
        }
        
        if(explosionBoolean){
            g.drawImage(m_AsteroidExplode, m_AsteroidCoordinates[currentAsteroid].getAstX(), m_AsteroidCoordinates[currentAsteroid].getAstY(), 100, 100, this);
        }
        
        if(game_over_message){
            g.setColor(Color.WHITE);
            g.drawString("score " + score, 15, 15);
        }
        
        if(!game_over_message){
            String msg = "FINAL SCORE " + score;
            Font fontsize = new Font("Impact", Font.ROMAN_BASELINE, 32);
            FontMetrics fm = getFontMetrics(fontsize);
            g.setColor(Color.WHITE);
            g.setFont(fontsize);
            g.drawString(msg, (m_FalconCoordinates.MAX_X - fm.stringWidth(msg))/2, m_FalconCoordinates.MAX_Y / 3);
        }
    }
    
    private class starTimerHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            moveStars();
            
            for(int i = 0; i < MAX_ASTEROIDS; i++){
                m_AsteroidCoordinates[i].moveAstDown(speedIncrease); //?
                if(destroyed > 3){
                    m_AsteroidCoordinates[i].moveAstDown(speedIncrease++);
                    destroyed = 0;
                }
            }
            checkCollision();
            
            if( m_AsteroidCoordinates[MAX_ASTEROIDS - 1].getAstY() > 1000 ){ game_over_message = false; }
            
            repaint();
        }        
    }
    
    private class FalconHandler implements KeyListener {
        
        public void keyTyped(KeyEvent e) {}
        
        public void keyPressed(KeyEvent e) {
            
            if(game_over_message){
            
                if( e.getKeyCode() == KeyEvent.VK_LEFT && m_FalconCoordinates.getX() > 0  ) 
                    m_FalconCoordinates.setX(m_FalconCoordinates.getX() - 5);
                
                if( e.getKeyCode() == KeyEvent.VK_RIGHT && m_FalconCoordinates.getY() < Coordinate.MAX_X - 50 )
                    m_FalconCoordinates.setX(m_FalconCoordinates.getX() + 5);
              
                if(e.getKeyCode() == KeyEvent.VK_SPACE) {
                    
                    int LaserAvail = -1;
                    
                    for(int i = 0; i < MAX_LASERS; i++)
                        if(m_Lasers[i].getVisible() == false)
                            LaserAvail = i;
                    if(LaserAvail >= 0){
                        m_Lasers[LaserAvail].setVisible(true);
                        clipLaser.setFramePosition(0);
                        clipLaser.start();
                    }
                }
                if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT){
                    repaint();
                }
            }
        }
        
        public void keyReleased(KeyEvent e) { }
    }
    
    public void checkCollision(){
        
        rectFalcon = new Rectangle(m_FalconCoordinates.getX(), m_FalconCoordinates.getY(), 100, 50);
        
        if(current_asteroid_Y <= m_AsteroidCoordinates[currentAsteroid].getAstY() + 1 ){ explosionBoolean = false; }
        
        for(int i = 0; i < MAX_ASTEROIDS; i++){
            
            if(m_AsteroidCoordinates[i].getAstY() > 0){
                rectAsteroid = new Rectangle(m_AsteroidCoordinates[i].getAstX() + 10, m_AsteroidCoordinates[i].getAstY() + 20, 80, 60);
                if(rectAsteroid.intersects(rectFalcon) && m_AsteroidCoordinates[i].getVisible() == true){
                    game_over_message = false;
                    m_StarTimer.stop();
                    clipCrash.loop(0);
                }
                
                for(int j = 0; j < MAX_LASERS; j++){
                    rectLaser = new Rectangle(m_Lasers[j].getX() - 1, m_Lasers[j].getY(), 3, 1);
                    if (rectAsteroid.intersects(rectLaser) && m_Lasers[j].getY() > 0 && m_AsteroidCoordinates[i].getVisible() == true){
                        
                        explosionBoolean = true;
                        currentAsteroid = i;
                        current_asteroid_Y = m_AsteroidCoordinates[i].getAstY();
                        
                        m_AsteroidCoordinates[i].explodeAsteroid(false);
                        
                        clipExplode.setFramePosition(0);
                        clipExplode.start();
                        
                        score += current_asteroid_Y;
                        destroyed++;
                    }
                }
            } 
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    //@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}