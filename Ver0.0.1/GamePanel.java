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

	player = new Jacket();
	bullets = new ArrayList<Bullet>();
	zombies = new ArrayList<Zombie>();

	for (int i = 0; i < 5; i++) {
	    zombies.add(new Zombie(1, 1));
	}

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

	player.update();

	for (int i = 0; i < bullets.size(); i++) {
	    boolean remove = bullets.get(i).update();
	    if (remove) {
		bullets.remove(i);
		i--;
	    }
	}

	for (int i = 0; i < zombies.size(); i++) {
	    zombies.get(i).update();
	}

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

	for (int i = 0; i < zombies.size(); i++) {
	    if (zombies.get(i).isDead()) {
		zombies.remove(i);
		i--;
	    }
	}
    }

    private void gameRender() {
	g.setColor(new Color(0, 100, 255));
	g.fillRect(0, 0, WIDTH, HEIGHT);
	g.setColor(Color.BLACK);
	g.drawString("FPS: " + averageFPS, 10, 10);
	g.drawString("Num Bullets: " + bullets.size(), 10, 20);

	player.draw(g);

	for (int i = 0; i < bullets.size(); i++) {
	    bullets.get(i).draw(g);
	}

	for (int i = 0; i < zombies.size(); i++) {
	    zombies.get(i).draw(g);
	}
    }

    private void gameDraw() {
	Graphics g2 = this.getGraphics();
	g2.drawImage(image, 0, 0, null);
	g2.dispose();
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
