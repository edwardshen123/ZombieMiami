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
    private boolean inMaskSelection;
    //variable of if masks are initialized
    private boolean maskInit;
    //variable for which mask is hovered over
    private int maskSelect;
    private boolean inTitleScreen;
    public static final String[] titleScreenSelection = {
	"Start Game",
	"Exit"
    };
    private int titleScreenSelect;
    private boolean inGame;
    private boolean pause;
    private boolean developerMode;
    private int FPS = 30;
    private double averageFPS;

    //Objects in the Game
    public static Jacket player;
    public static ArrayList<Mask> masks;
    public static ArrayList<Bullet> bullets;
    public static ArrayList<Rocket> rockets;
    public static ArrayList<Grenade> grenades;
    public static ArrayList<Zombie> zombies;
    public static ArrayList<Weapon> weapons;
    public static ArrayList<Explosion> explosions;

    //Wave Variables
    private long waveStartTimer;
    private long waveStartTimerDiff;
    private int waveNumber;
    private boolean waveStart;
    private int waveDelay = 2000;

    //Grenade Variables
    //Currently equipped grenade type
    public static int grenadeType;
    //Is Holy Hand Grenade on Screen
    public static boolean hhgOnScreen;
    private int grenadeTextTimer = 2000;

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
	masks = new ArrayList<Mask>();
	bullets = new ArrayList<Bullet>();
	rockets = new ArrayList<Rocket>();
	grenades = new ArrayList<Grenade>();
	zombies = new ArrayList<Zombie>();
	weapons = new ArrayList<Weapon>();
	explosions = new ArrayList<Explosion>();

	//Wave Timers and Number
	waveStartTimer = 0;
	waveStartTimerDiff = 0;
	waveStart = true;
	waveNumber = 0;

	//Grenade
	grenadeType = 0;
	hhgOnScreen = false;

	long startTime;
	long URDTimeMillis;
	long waitTime;
	long totalTime = 0;

	//Game Information
	developerMode = false;
	inMaskSelection = true;
	maskInit = false;
	maskSelect = -100;
	pause = false;
	inTitleScreen = true;
	titleScreenSelect = 0;
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

	//Title Screen
	if (inTitleScreen) {return;}

	//Game Pause
	if (pause) {return;}

	//Mask Select Update
	if (inMaskSelection) {
	    if (!maskInit) {
		double firstX = WIDTH / (Mask.numsOfMask + 1);
		double firstY = HEIGHT / 2;
		for (int i = 0; i < Mask.numsOfMask; i++) {
		    masks.add(new Mask(firstX * (1 + i), firstY, i));
		}
		maskInit = true;
	    }
	    for (int i = 0; i < masks.size(); i++) {
		masks.get(i).update();
	    }
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

	//Grenade Update
	for (int i = 0; i < grenades.size(); i++) {
	    Grenade gr = grenades.get(i);
	    boolean remove = gr.update();
	    if (remove) {
		grenades.remove(i);
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

	//Explosions Update
	for (int i = 0; i < explosions.size(); i++) {
	    boolean remove = explosions.get(i).update();
	    if (remove) {
		explosions.remove(i);
		i--;
	    }
	}

	//Grenades Movement
	for (int i = 0; i < grenades.size(); i++) {
	    Grenade gr = grenades.get(i);
	    if (!gr.atMaxRange) {
		double grx = gr.getX();
		double gry = gr.getY();

		double delX = grx - gr.initX;
		double delY = gry - gr.initY;
		double dist = Math.sqrt(delX * delX + delY * delY);
		if (dist >= gr.maxRange) {
		    gr.dx = 0;
		    gr.dy = 0;
		    gr.atMaxRange = true;
		    gr.grenadeCountdownStartTime = System.nanoTime(); 
		}
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
		explosions.add(new Explosion(rx, ry,(int) rr,(int) (rr + rer)));
	    }
	}

	//Grenade to Zombie Collision
	for (int i = 0; i < grenades.size(); i++) {
	    Grenade gr = grenades.get(i);

	    if (gr.atMaxRange) {
		long elapsed = System.nanoTime() - gr.grenadeCountdownStartTime;
		if (elapsed / Conversion >= gr.explosionDelay) {
		    double grx = gr.getX();
		    double gry = gr.getY();
		    double grr = gr.getR();
		    double grer = gr.getER();

		    for (int j = 0; j < zombies.size(); j++) {
			Zombie z = zombies.get(j);
			double zx = z.getX();
			double zy = z.getY();
			double zr = z.getR();

			double dx = grx - zx;
			double dy = gry - zy;
			double dist = Math.sqrt(dx * dx + dy * dy);

			if (dist < grr + grer + zr) {
			    z.hit(2);
			}
		    }
		    grenades.remove(i);
		    i--;
		    if (gr.isFirstHolyHandGrenade) {
			hhgOnScreen = false;
		    }
		    explosions.add(new Explosion(grx, gry, (int) grr, (int) (grr + grer)));
		}
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
		    weapons.add(new Weapon(12, z.getX(), z.getY()));
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

	//Check Dead Player
	if (player.getLives() <= 0) {
	    running = false;
	}

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

	if (!inGame) {
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

	    //Draw title screen
	    if (inTitleScreen) {
		g.setColor(Color.GREEN);
		g.setFont(new Font("Century Gothic", Font.BOLD, 20));
		String s = "Zombie Miami";
		int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
		g.drawString(s, WIDTH / 2 - length / 2, HEIGHT / 2 - 50);
		g.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		for (int i = 0; i < titleScreenSelection.length; i++) {
		    if (i == titleScreenSelect) {
			g.setColor(Color.GREEN);
		    } else {
			g.setColor(Color.WHITE);
		    }
		    s = titleScreenSelection[i];
		    length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
		    g.drawString(s, WIDTH / 2 - length / 2, HEIGHT / 2 + i * 20);
		}
		return;
	    }

	    //Draw mask select
	    if (inMaskSelection) {
		for (int i = 0; i < masks.size(); i++) {
		    masks.get(i).draw(g);
		}
		g.setColor(Color.WHITE);
		g.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		String s = "--S e l e c t   Y o u r   M a s k -- ";
		int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
		g.drawString(s, WIDTH / 2 - length / 2, HEIGHT / 2 - 40);
		//Mask Description
		g.setFont(new Font("Century Gothic", Font.PLAIN, 15));
		if (maskSelect == -100) {
		    s = "";
		} else {
		    s = masks.get(maskSelect).toString();
		}
		length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
		g.drawString(s, WIDTH / 2 - length / 2, HEIGHT / 2 + 50); 
		return;
	    }
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

	//Draw grenades
	for (int i = 0; i < grenades.size(); i++) {
	    grenades.get(i).draw(g);
	    if (hhgOnScreen && grenades.get(i).isFirstHolyHandGrenade) {
		//Monty Python
		g.setFont(new Font("Century Gothic", Font.PLAIN, 14));
		long grenadeStartTimerDiff = (System.nanoTime() - grenades.get(i).grenadeReleaseStartTime) / Conversion;
		int alpha = (int) (255 * Math.sin(3.14 * grenadeStartTimerDiff / grenadeTextTimer));
		if (alpha >= 255) { 
		    alpha = 255; 
		}
		g.setColor(new Color(255, 255, 255, alpha));
		String s = "lobbest though thy holy hand grenade of Antioch";
		int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
		g.drawString(s, WIDTH / 2 - length / 2, HEIGHT - 50);
		s = " towards thou foe,";
		length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
		g.drawString(s, WIDTH / 2 - length / 2, HEIGHT - 30);
		s = "who being naughty in my sight, shall snuff it";
		length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
		g.drawString(s, WIDTH / 2 - length / 2, HEIGHT - 10);
	    }
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

	//draw number of grenades
	g.setColor(Color.WHITE);
	g.setFont(new Font("Century Gothic", Font.PLAIN, 14));
	String s = player.grenadeNames[grenadeType] + ": " + player.getGrenadeNum(grenadeType);
	int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
	g.drawString(s, WIDTH - length - 42, 50);
	if (player.grenadeNames[grenadeType] == "HHG of A") {
	    s = "The Holy Hand Grenade of Antioch";
	    length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
	    g.drawString(s, WIDTH - length - 42, 70);
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
	    s = "   P  A  U  S  E   ";
	    length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
	    g.setFont(new Font("Century Gothic", Font.PLAIN, 18));
	    g.drawString(s, WIDTH / 2 - length / 2 - 20, HEIGHT / 2);
	    s = " Press P or Esc to Unpause ";
	    length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
	    g.setFont(new Font("Century Gothic", Font.PLAIN, 14));
	    g.drawString(s, WIDTH / 2 - length / 2 + 25, HEIGHT / 2 + 20);
	    return;
	}
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
	if (keyCode == KeyEvent.VK_Q) {
	    if (grenadeType <= 0) {
		grenadeType = 2;
	    } else {
		grenadeType--;
	    }
	}
	if (keyCode == KeyEvent.VK_E) {
	    if (grenadeType >= 2) {
		grenadeType = 0;
	    } else {
		grenadeType++;
	    }
	}
	if (keyCode == KeyEvent.VK_LEFT) {
	    if (inMaskSelection && maskInit) {
		if (maskSelect == -100) {
		    maskSelect = masks.size() - 1;
		    masks.get(maskSelect).setSelect(true);
		} else if (maskSelect <= 0) {
		    masks.get(maskSelect).setSelect(false);
		    maskSelect = masks.size() - 1;
		    masks.get(maskSelect).setSelect(true);
		} else {
		    masks.get(maskSelect).setSelect(false);
		    maskSelect--;
		    masks.get(maskSelect).setSelect(true);
		}
	    }
	}
	if (keyCode == KeyEvent.VK_RIGHT) {
	    if (inMaskSelection && maskInit) {
		if (maskSelect == -100) {
		    maskSelect = 0;
		    masks.get(maskSelect).setSelect(true);
		} else if (maskSelect >= masks.size() - 1) {
		    masks.get(maskSelect).setSelect(false);
		    maskSelect = 0;
		    masks.get(maskSelect).setSelect(true);
		} else {
		    masks.get(maskSelect).setSelect(false);
		    maskSelect++;
		    masks.get(maskSelect).setSelect(true);
		}
	    }
	}
	if (keyCode == KeyEvent.VK_UP) {
	    if (inTitleScreen) {
		if (titleScreenSelect <= 0) {
		    titleScreenSelect = titleScreenSelection.length - 1;
		} else {
		    titleScreenSelect--;
		}
	    }
	}
	if (keyCode == KeyEvent.VK_DOWN) {
	    if (inTitleScreen) {
		if (titleScreenSelect >= titleScreenSelection.length - 1) {
		    titleScreenSelect = 0;
		} else {
		    titleScreenSelect++;
		}
	    }
	}
	if (keyCode == KeyEvent.VK_ENTER) {
	    if (inMaskSelection && maskInit && maskSelect != -100) {
		//add mask to jacket
		Mask m = masks.get(maskSelect);
		player.setMaskBoost(m.getBoostType(), m.getBoost());
		inMaskSelection = false;
	    }
	    if (inTitleScreen) {
		switch (titleScreenSelect) {
		    case 0:
			inTitleScreen = false;
			break;
		    case 1:
			System.exit(0);
			break;
		}
	    }
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
	int button = mouse.getButton();
	double x = mouse.getX();
	double y = mouse.getY();
	if (button == MouseEvent.BUTTON1) {
	    player.setFiring(true, x, y, false);
	}
	if (button == MouseEvent.BUTTON3) {
	    player.setFiring(true, x, y, true);
	}
    }
    public void mouseReleased(MouseEvent mouse) {
	int button = mouse.getButton();
	if (button == MouseEvent.BUTTON1) {
	    player.setFiring(false, 0, 0, false);
	}
	if (button == MouseEvent.BUTTON3) {
	    player.setFiring(false, 0, 0, true);
	}
    }
}
