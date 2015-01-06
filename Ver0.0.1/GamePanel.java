import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.*;

public class GamePanel extends JPanel implements Runnable {

    public static int WIDTH = 400;
    public static int HEIGHT = 400;

    private Thread thread;
    private boolean running;

    private BufferedImage image;
    private Graphics2D g;

    private int FPS = 30;
    private double averageFPS;

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
    }

    public void run() {
	running = true;
	image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	g = (Graphics2D) image.getGraphics();

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
    }

    private void gameRender() {
	g.setColor(Color.WHITE);
	g.fillRect(0, 0, WIDTH, HEIGHT);
	g.setColor(Color.BLACK);
	g.drawString("FPS: " + averageFPS, 10, 10);
    }

    private void gameDraw() {
	Graphics g2 = this.getGraphics();
	g2.drawImage(image, 0, 0, null);
	g2.dispose();
    }
}
