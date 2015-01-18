import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable, KeyListener, MouseListener {

    //Dimensions
    /*
    private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static final double screenWidth = screenSize.getWidth();
    private static final double screenHeight = screenSize.getHeight();
    public static int WIDTH =(int)screenWidth - 100;
    public static int HEIGHT =(int)screenHeight - 100;
    */
    //Testing Dimensions
    public static int WIDTH = 400;
    public static int HEIGHT = 400;

    //Thread Variables
    private Thread thread;
    private boolean running;

    //Graphics
    /*
      BufferedImage allows for the rendering of the game image onto an off-screen memory location. Then through gameDraw(), it will copy over from that memory to the screen. So it completes the drawing first before bringing it all over as one single image to the screen.
    */
    private BufferedImage image;
    /*
      The Canvas painted on
    */
    private Graphics2D g;

    //Game Information
    private boolean maskSelect;
    private boolean inGame;
    private boolean pause;
    private boolean developerMode;
    private int FPS = 30;
    private double averageFPS;

    //Objects in the Game
    public static Jacket player;
    public static ArrayList<Bullet> bullets;
    public static ArrayList<Rocket> rockets;
    public static ArrayList<Zombie> zombies;
    public static ArrayList<Weapon> weapons;
    public static ArrayList<Explosion> explosions;

    //Wave Variables
    private long waveStartTimer;
    private long waveStartTimerDiff;
    private int waveNumber;
    private boolean waveStart;
    private int waveDelay = 2000;

    //Conversion Variable
    private static final int Conversion = 1000000;

    //Temp Variables
    private boolean isTest = true;

    //Constructor
    public GamePanel() {
	super();
	setPreferredSize(new Dimension(WIDTH, HEIGHT));
	setFocusable(true);
	requestFocus();
    }

    //Start Thread
    public void addNotify() {
	super.addNotify();
	if (thread == null) {
	    thread = new Thread(this);
	    thread.start();
	}
	//initial Listeners
	addKeyListener(this);
	addMouseListener(this);
    }

    //Game Engine
    public void run() {
	running = true;
	image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	g = (Graphics2D) image.getGraphics();
	//Game Antialiasing
	g.setRenderingHint(
			   RenderingHints.KEY_ANTIALIASING, 
			   RenderingHints.VALUE_ANTIALIAS_ON);
	g.setRenderingHint(
			   RenderingHints.KEY_TEXT_ANTIALIASING,
			   RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

	//Setting up Objects
	player = new Jacket();
	bullets = new ArrayList<Bullet>();
	rockets = new ArrayList<Rocket>();
	zombies = new ArrayList<Zombie>();
	weapons = new ArrayList<Weapon>();
	explosions = new ArrayList<Explosion>();

	//Wave Timers and Number
	waveStartTimer = 0;
	waveStartTimerDiff = 0;
	waveStart = true;
	waveNumber = 0;

	long startTime;
	long URDTimeMillis;
	long waitTime;
	long totalTime = 0;

	//Game Information
	developerMode = false;
	maskSelect = true;
	pause = false;
	inGame = false;

	//FPS Counts
	int frameCount = 0;
	int maxFrameCount = 30;

	long targetTime = 1000 / FPS;

	//Game Loop
	while (running) {

	    startTime = System.nanoTime();

	    //Game Update and Draw
	    gameUpdate();
	    gameRender();
	    gameDraw();
	    
	    //Game Playability Pause
	    URDTimeMillis = (System.nanoTime() - startTime) / Conversion;
	    waitTime = targetTime - URDTimeMillis;

	    try {
		Thread.sleep(waitTime);
	    } catch (Exception e) {
	    }

	    //FPS count
	    totalTime += System.nanoTime() - startTime;
	    frameCount++;
	    if (frameCount == maxFrameCount) {
		averageFPS = 1000.0 / ((totalTime / frameCount) / Conversion);
		frameCount = 0;
		totalTime = 0;
	    }
	}

	//Game Over
	g.setColor(new Color(255, 120, 120));
	g.fillRect(0, 0, WIDTH, HEIGHT);
	g.setColor(new Color(255, 0, 0));
	g.setFont(new Font("Century Gothic", Font.PLAIN, 36));
	String s = "   G a m e   O v e r   ";
	int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
	g.drawString(s, (WIDTH - length) / 2, HEIGHT / 2);
	g.setFont(new Font("Century Gothic", Font.PLAIN, 18));
	s = "Total Score = " + player.getScore();
	length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
	g.drawString(s, (WIDTH - length) / 2, HEIGHT / 2 + 30);
	gameDraw();
    }

    private void gameUpdate() {
	/*
	//Title Screen
	if (!inGame) {
	    
	    return;
	}
	*/

	//Game Pause
	if (pause) {return;}

	//Mask Select
	if (maskSelect) {
	    return;
	}

	//Wave Update
	if (waveStartTimer == 0 && zombies.size() == 0) {
	    waveNumber++;
	    waveStart = false;
	    waveStartTimer = System.nanoTime();
	} else {
	    waveStartTimerDiff = (System.nanoTime() - waveStartTimer) / Conversion;
	    if (waveStartTimerDiff > waveDelay) {
		waveStart = true;
		waveStartTimer = 0;
		waveStartTimerDiff = 0;
	    }
	}

	//Zombie Creation
	if (waveStart && zombies.size() == 0) {
	    createNewZombies();
	}
	
	/*
	if (isTest) {
	    createNewZombies();
	    isTest = false;
	    System.out.println(true);
	}
	*/
	//Player Update
	player.update();

	//Bullet Update
	for (int i = 0; i < bullets.size(); i++) {
	    boolean remove = bullets.get(i).update();
	    if (remove) {
		bullets.remove(i);
		i--;
	    }
	}

	//Rocket Update
	for (int i = 0; i < rockets.size(); i++) {
	    Rocket r = rockets.get(i);
	    boolean remove = r.update();
	    if (remove) {
		rockets.remove(i);
		i--;
	    }
	}

	//Zombie Update
	for (int i = 0; i < zombies.size(); i++) {
	    zombies.get(i).update(player);
	}

	//Weapons Update
	for (int i = 0; i < weapons.size(); i++) {
	    boolean remove = weapons.get(i).update();
	    if (remove) {
		weapons.remove(i);
		i--;
	    }
	}

	//Explosion Update
	for (int i = 0; i < explosions.size(); i++) {
	    boolean remove = explosions.get(i).update();
	    if (remove) {
		explosions.remove(i);
		i--;
	    }
	}

	//Zombie to Bullet Collision
	for (int i = 0; i < bullets.size(); i++) {
	    Bullet b = bullets.get(i);
	    double bx = b.getX();
	    double by = b.getY();
	    double br = b.getR();

	    for (int j = 0; j < zombies.size(); j++) {
		Zombie z = zombies.get(j);
		double zx = z.getX();
		double zy = z.getY();
		double zr = z.getR();

		double dx = bx - zx;
		double dy = by - zy;
		double dist = Math.sqrt(dx * dx + dy * dy);

		if (dist < br + zr) {
		    z.hit(1);
		    bullets.remove(i);
		    i--;
		    break;
		}
	    }
	}

	//Zombie to Rocket Collision
	for (int i = 0; i < rockets.size(); i++) {
	    Rocket r = rockets.get(i);
	    double rx = r.getX();
	    double ry = r.getY();
	    double rr = r.getR();
	    double rer = r.getER();
	    boolean isRocketHit = false;

	    for (int j = 0; j < zombies.size(); j++) {
		Zombie z = zombies.get(j);
		double zx = z.getX();
		double zy = z.getY();
		double zr = z.getR();

		double dx = rx - zx;
		double dy = ry - zy;
		double dist = Math.sqrt(dx * dx + dy * dy);

		if (dist < rr + rer + zr) {
		    z.hit(2);
		    if (!isRocketHit) {
			isRocketHit = true;
			r.explode();
		    }
		}
	    }
	    if (isRocketHit) {
		rockets.remove(i);
		i--;
		explosions.add(new Explosion(r.getX(), r.getY(),(int) r.getR(),(int) r.getR() + 20));
	    }
	}

	//Clean dead zombies
	for (int i = 0; i < zombies.size(); i++) {
	    if (zombies.get(i).isDead()) {

		Zombie z = zombies.get(i);

		//weapon drop
		double rand = Math.random();
		if (rand < 0.001) {
		    weapons.add(new Weapon(1, z.getX(), z.getY()));
		} else if (rand < 0.100) {
		    weapons.add(new Weapon(2, z.getX(), z.getY()));
		} else if (rand < 0.200) {
		    weapons.add(new Weapon(3, z.getX(), z.getY()));
		} else if (rand < 0.210) {
		    weapons.add(new Weapon(4, z.getX(), z.getY()));
		} else if (rand < 0.310) {
		    weapons.add(new Weapon(5, z.getX(), z.getY()));
		} else if (rand < 0.330) {
		    weapons.add(new Weapon(6, z.getX(), z.getY()));
		} else if (rand < 0.430) {
		    weapons.add(new Weapon(7, z.getX(), z.getY()));
		} else if (rand < 0.440) {
		    weapons.add(new Weapon(8, z.getX(), z.getY()));
		} else if (rand < 0.540) {
		    weapons.add(new Weapon(9, z.getX(), z.getY()));
		} else if (rand < 0.590) {
		    weapons.add(new Weapon(10, z.getX(), z.getY()));
		} else if (rand < 0.690) {
		    weapons.add(new Weapon(11, z.getX(), z.getY()));
		} else if (rand < 0.691) {
		    weapons.add(new Weapon(12, z.getY(), z.getY()));
		}

		//score and clean
		player.addScore(z.getRank() + z.getType());
		zombies.remove(i);
		i--;
		
		//exploding enemies
		if(z.explode()) {
		    explosions.add(new Explosion(z.getX(), z.getY(),(int) z.getR(),(int) z.getR() + 20));
		    //Exploding damage
		    if (!player.isRecovering()) {
			double px = player.getX();
			double py = player.getY();
			double pr = player.getR();
			
			double zx = z.getX();
			double zy = z.getY();
			double zr = z.getR();
			double zer = z.getER();

			double dx = px - zx;
			double dy = py - zy;
			double dist = Math.sqrt(dx * dx + dy * dy);
		
			if (dist < pr + zr + zer) {
			    player.loseLife();
			}
		    }
		}
	    }
	}

	//Zombie to Player Collision
	if (!player.isRecovering()) {
	    double px = player.getX();
	    double py = player.getY();
	    double pr = player.getR();
	    for (int i = 0; i < zombies.size(); i++) {
		Zombie z = zombies.get(i);
		double zx = z.getX();
		double zy = z.getY();
		double zr = z.getR();
		double zer = z.getER();

		double dx = px - zx;
		double dy = py - zy;
		double dist = Math.sqrt(dx * dx + dy * dy);
		
		if (dist < pr + zr) {
		    player.loseLife();
		}
	    }
	}

	/* Removed for testing purposes
	//Check Dead Player
	if (player.getLives() <= 0) {
	    running = false;
	}
	*/

	//Weapon to Player Collision
	double px = player.getX();
	double py = player.getY();
	double pr = player.getR();
	for (int i = 0; i < weapons.size(); i++) {
	    Weapon w = weapons.get(i);
	    double wx = w.getX();
	    double wy = w.getY();
	    double wr = w.getR();

	    double dx = px - wx;
	    double dy = py - wy;
	    double dist = Math.sqrt(dx * dx + dy * dy);
		
	    if (dist < pr + wr) {

		player.setWeapon(w);
		
		weapons.remove(i);
		i--;
	    }
	}
    }

    private void gameRender() {

	/*
	//Draw title screen
	if (!inGame) {
	    
	    return;
	}
	*/

	//Draw mask select
	if (maskSelect) {
	    
	    return;
	}

	//Draw pause screen
	if (pause) {
	    g.setColor(new Color(102, 178, 255));
	    //sets transparency because setColor(new Color(R, G, B, A)) doesn't work
	    /*
	      This breaks the game
	      To be fixed
	    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.05f));
	    */
	    /*
	    g.fillRect(centerWIDTH - 100, centerHEIGHT - 100, 200, 200);
	    */
	    String s = "   P  A  U  S  E   ";
	    int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
	    g.setFont(new Font("Century Gothic", Font.PLAIN, 18));
	    g.drawString(s, WIDTH / 2 - length / 2 - 20, HEIGHT / 2);
	    s = " Press P or Esc to Unpause ";
	    length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
	    g.setFont(new Font("Century Gothic", Font.PLAIN, 14));
	    g.drawString(s, WIDTH / 2 - length / 2 + 25, HEIGHT / 2 + 20);
	    return;
	}

	//Draw background
	g.setColor(new Color(0, 100, 255));
	g.fillRect(0, 0, WIDTH, HEIGHT);

	//Developer stats
	if (developerMode) {
	    g.setColor(Color.WHITE);
	    g.setFont(new Font("Century Gothic", Font.PLAIN, 14));
	    g.drawString("FPS: " + averageFPS, 20, 70);
	    g.drawString("Num Bullets: " + bullets.size(), 20, 90);
	}

	//Draw player
	player.draw(g);

	//Draw bullets
	for (int i = 0; i < bullets.size(); i++) {
	    bullets.get(i).draw(g);
	}

	//Draw rockets
	for (int i = 0; i < rockets.size(); i++) {
	    rockets.get(i).draw(g);
	}

	//Draw zombies
	for (int i = 0; i < zombies.size(); i++) {
	    zombies.get(i).draw(g);
	}

	//Draw weapons
	for (int i = 0; i < weapons.size(); i++) {
	    weapons.get(i).draw(g);
	}

	//draw wave number
	if (waveStartTimer != 0) {
	    g.setFont(new Font("Century Gothic", Font.PLAIN, 18));
	    String s = " - W A V E   " + waveNumber + "   -";
	    int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
	    //Fade out
	    int alpha = (int) (255 * Math.sin(3.14 * waveStartTimerDiff / waveDelay));
	    if (alpha > 255) { alpha = 255; }
	    g.setColor(new Color(255, 255, 255, alpha));
	    g.drawString(s, WIDTH / 2 - length / 2, HEIGHT / 2);
	}

	//draw lives
	for (int i = 0; i < player.getLives(); i++) {
	    g.setColor(Color.WHITE);
	    g.fillOval(20 + ( 20 * i ), 20, (int) player.getR() * 2, (int) player.getR() * 2);
	    g.setStroke(new BasicStroke(3));
	    g.setColor(Color.WHITE.darker());
	    g.drawOval(20 + ( 20 * i ), 20, (int) player.getR() * 2, (int) player.getR() * 2);
	    g.setStroke(new BasicStroke(1));
	}

	//draw weapon name
	g.setColor(Color.WHITE);
	g.setFont(new Font("Century Gothic", Font.PLAIN, 14));
	g.drawString("Weapon: " + player.getWeaponName(), 20, 50);

	//draw explosions
	for (int i = 0; i < explosions.size(); i++) {
	    explosions.get(i).draw(g);
	}

	//draw score
	g.setColor(Color.WHITE);
	g.setFont(new Font("Century Gothic", Font.PLAIN, 14));
	g.drawString("Score: " + player.getScore(), WIDTH - 100, 30);
    }

    private void gameDraw() {
	Graphics g2 = this.getGraphics();
	g2.drawImage(image, 0, 0, null);
	g2.dispose();
    }

    private void createNewZombies() {

	zombies.clear();
	
	/*
	if (waveNumber == 1) {
	    for (int i = 0; i < 5; i++) {
		zombies.add(new Zombie(1, 1));
	    }
	}
	if (waveNumber == 2) {
	    for (int i = 0; i < 10; i++) {
		zombies.add(new Zombie(1, 1));
	    }
	}
	*/
	for (int i = 0; i < waveNumber; i++) {
	    zombies.add(new Zombie(4, 1));
	    zombies.add(new Zombie(4, 1));
	    if (i != 0) {
		if (i%2 == 0) {
		    zombies.add(new Zombie(4, 1));
		}
		if (i%3 == 0) {
		    zombies.add(new Zombie(4, 1));
		}
		if (i%4 == 0) {
		    zombies.add(new Zombie(4, 1));
		}
	    }
	}

    }

    public void keyTyped(KeyEvent key) {
    }
    public void keyPressed(KeyEvent key) {
	int keyCode = key.getKeyCode();
	if (keyCode == KeyEvent.VK_A) {
	    player.setLeft(true);
	}
	if (keyCode == KeyEvent.VK_D) {
	    player.setRight(true);
	}
	if (keyCode == KeyEvent.VK_W) {
	    player.setUp(true);
	}
	if (keyCode == KeyEvent.VK_S) {
	    player.setDown(true);
	}
	/*
	if (keyCode == KeyEvent.VK_SPACE) {
	    player.setFiring(true);
	}
	*/
	if (keyCode == KeyEvent.VK_P) {
	    if (!pause) {
		pause = true;
	    } else {
		pause = false;
	    }
	}
	if (keyCode == KeyEvent.VK_ESCAPE) {
	    if (pause) {
		pause = false;
	    }
	}
	if (keyCode == KeyEvent.VK_F1) {
	    if (!developerMode) {
		developerMode = true;
	    } else {
		developerMode = false;
	    }
	}
    }
    public void keyReleased(KeyEvent key) {
	int keyCode = key.getKeyCode();
	if (keyCode == KeyEvent.VK_A) {
	    player.setLeft(false);
	}
	if (keyCode == KeyEvent.VK_D) {
	    player.setRight(false);
	}
	if (keyCode == KeyEvent.VK_W) {
	    player.setUp(false);
	}
	if (keyCode == KeyEvent.VK_S) {
	    player.setDown(false);
	}
	/*
	if (keyCode == KeyEvent.VK_SPACE) {
	    player.setFiring(false);
	}
	*/
    }
    public void mouseEntered(MouseEvent mouse) {
	pause = false;
    }
    public void mouseExited(MouseEvent mouse) {
	pause = true;
    }
    public void mouseClicked(MouseEvent mouse) {
    }
    public void mousePressed(MouseEvent mouse) {
	double x = mouse.getX();
	double y = mouse.getY();
	player.setFiring(true, x, y);
	
    }
    public void mouseReleased(MouseEvent mouse) {
	player.setFiring(false, 0, 0);
    }
}
