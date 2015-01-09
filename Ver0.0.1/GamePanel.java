import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    public static int WIDTH = 400;
    public static int HEIGHT = 400;

    private Thread thread;
    private boolean running;

    private BufferedImage image;
    private Graphics2D g;

    private int FPS = 30;
    private double averageFPS;

    public static Jacket player;
    public static ArrayList<Bullet> bullets;
    public static ArrayList<Zombie> zombies;
    public static ArrayList<Weapon> weapons;

    private long waveStartTimer;
    private long waveStartTimerDiff;
    private int waveNumber;
    private boolean waveStart;
    private int waveDelay = 2000;

    private int Conversion = 1000000;

    public GamePanel() {
	super();
	setPreferredSize(new Dimension(WIDTH, HEIGHT));
	setFocusable(true);
	requestFocus();
    }

    public void addNotify() {
	super.addNotify();
	if (thread == null) {
	    thread = new Thread(this);
	    thread.start();
	}
	addKeyListener(this);
    }

    public void run() {
	running = true;
	image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	g = (Graphics2D) image.getGraphics();
	g.setRenderingHint(
			   RenderingHints.KEY_ANTIALIASING, 
			   RenderingHints.VALUE_ANTIALIAS_ON);
	g.setRenderingHint(
			   RenderingHints.KEY_TEXT_ANTIALIASING,
			   RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

	player = new Jacket();
	bullets = new ArrayList<Bullet>();
	zombies = new ArrayList<Zombie>();
	weapons = new ArrayList<Weapon>();

	waveStartTimer = 0;
	waveStartTimerDiff = 0;
	waveStart = true;
	waveNumber = 0;

	long startTime;
	long URDTimeMillis;
	long waitTime;
	long totalTime = 0;

	int frameCount = 0;
	int maxFrameCount = 30;

	long targetTime = 1000 / FPS;

	while (running) {

	    startTime = System.nanoTime();

	    gameUpdate();
	    gameRender();
	    gameDraw();

	    URDTimeMillis = (System.nanoTime() - startTime) / Conversion;
	    waitTime = targetTime - URDTimeMillis;

	    try {
		Thread.sleep(waitTime);
	    } catch (Exception e) {
	    }

	    totalTime += System.nanoTime() - startTime;
	    frameCount++;
	    if (frameCount == maxFrameCount) {
		averageFPS = 1000.0 / ((totalTime / frameCount) / Conversion);
		frameCount = 0;
		totalTime = 0;
	    }
	}
    }

    private void gameUpdate() {
	//new wave
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
	//Zombie Update
	for (int i = 0; i < zombies.size(); i++) {
	    zombies.get(i).update();
	}

	//Weapons Update
	for (int i = 0; i < weapons.size(); i++) {
	    boolean remove = weapons.get(i).update();
	    if (remove) {
		weapons.remove(i);
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
		    z.hit();
		    bullets.remove(i);
		    i--;
		    break;
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
		} else if (rand < 0.020) {
		    weapons.add(new Weapon(2, z.getX(), z.getY()));
		} else if (rand < 0.120) {
		    weapons.add(new Weapon(3, z.getX(), z.getY()));
		}

		player.addScore(1);
		zombies.remove(i);
		i--;
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

		double dx = px - zx;
		double dy = py - zy;
		double dist = Math.sqrt(dx * dx + dy * dy);
		
		if (dist < pr + zr) {
		    player.loseLife();
		}
	    }
	}
    }

    private void gameRender() {

	//draw background
	g.setColor(new Color(0, 100, 255));
	g.fillRect(0, 0, WIDTH, HEIGHT);

	//Developer Stats
	/*
	g.setColor(Color.BLACK);
	g.drawString("FPS: " + averageFPS, 10, 10);
	g.drawString("Num Bullets: " + bullets.size(), 10, 20);
	*/

	//Draw player
	player.draw(g);

	//Draw bullets
	for (int i = 0; i < bullets.size(); i++) {
	    bullets.get(i).draw(g);
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
	for (int i = 0; i < waveNumber * 3; i++) {
	    zombies.add(new Zombie(1, 1));
	}

    }

    public void keyTyped(KeyEvent key) {
    }
    public void keyPressed(KeyEvent key) {
	int keyCode = key.getKeyCode();
	if (keyCode == KeyEvent.VK_LEFT) {
	    player.setLeft(true);
	}
	if (keyCode == KeyEvent.VK_RIGHT) {
	    player.setRight(true);
	}
	if (keyCode == KeyEvent.VK_UP) {
	    player.setUp(true);
	}
	if (keyCode == KeyEvent.VK_DOWN) {
	    player.setDown(true);
	}
	if (keyCode == KeyEvent.VK_Z) {
	    player.setFiring(true);
	}
    }
    public void keyReleased(KeyEvent key) {
	int keyCode = key.getKeyCode();
	if (keyCode == KeyEvent.VK_LEFT) {
	    player.setLeft(false);
	}
	if (keyCode == KeyEvent.VK_RIGHT) {
	    player.setRight(false);
	}
	if (keyCode == KeyEvent.VK_UP) {
	    player.setUp(false);
	}
	if (keyCode == KeyEvent.VK_DOWN) {
	    player.setDown(false);
	}
	if (keyCode == KeyEvent.VK_Z) {
	    player.setFiring(false);
	}
    }
}
