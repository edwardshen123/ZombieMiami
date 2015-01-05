import javax.swing.JFrame;
import java.awt.*;
import java.awt.image.*;

public class GamePanel extends JFrame implements Runnable {

    public static int WIDTH = 400;
    public static int HEIGHT = 400;

    private Thread thread;
    private boolean running;

    public GamePanel() {
	super();
	setPreferredSize(new Dimension(WIDTH, HEIGHT));
	setFocusable(true);
	requestFocus();
    }

    public void addNotify() {
	super.addNotify();
	if (thread = null) {
	    thread = new Thread(this);
	    thread.start();
	}
    }

    public void run() {
	running = true;
	while (running) {
	    gameUpdate();
	    gameRender();
	    gameDraw();
	}
    }

    private void gameUpdate() {
    }
}
