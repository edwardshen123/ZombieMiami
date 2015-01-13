import java.awt.*;

public class Zombie {

    private double x;
    private double y;
    private int r;
    private int er = 0;
    
    private double dx;
    private double dy;
    private double rad;
    private double speed;

    private int health;
    private int type;
    private int rank;

    private Color color1;
    
    private boolean ready;
    private boolean dead;

    private boolean hit;
    private long hitTimer;

    private static final int Conversion = 1000000;

    public Zombie(int type, int rank) {
	this.type = type;
	this.rank = rank;
	
	//Walkers
	if (type == 1) {
	    color1 = Color.GREEN;
	    if (rank == 1) {
		speed = 2;
		r = 5;
		health = 1;
	    }
	}
	
	//Police Walkerss
	if (type == 2) {
	    color1 = Color.RED;
	    if (rank == 1) {
		speed = 4;
		r = 5;
		health = 3;
	    }
	}

	//Chargers
	if (type == 3) {
	    color1 = Color.GRAY;
	    if (rank == 1) {
		speed = 6;
		r = 7;
		health = 6;
	    }
	}

	//Explosive Zombie
	if (type == 4) {
	    color1 = Color.RED;
	    if (rank == 1) {
		speed = 2;
		r = 8;
		health = 5;
	    }
	}

	//Initiate Movement
	x = Math.random() * GamePanel.WIDTH / 2 + GamePanel.WIDTH / 4;
	y = -r;
	
	double angle = Math.random() * 140 + 20;
	rad = Math.toRadians(angle);
	
	dx = Math.cos(rad) * speed;
	dy = Math.sin(rad) * speed;

	ready = false;
	dead = false;
    }

    public double getX() {return x;}
    public double getY() {return y;}
    public double getR() {return r;}
    public double getER() {return er;}

    public void hit() {
	health--;
	if (health <= 0) {
	    dead = true;
	}
	hit = true;
	hitTimer = System.nanoTime();
    }
    
    public void explode() {
	if (type == 4) {
	    er = 5;
	}
    }

    public boolean isDead() {
	return dead;
    }
    
    public void update(Jacket player) {

	x += dx;
	y += dy;

	//tracking AI
	double px = player.getX();
	double py = player.getY();

	double angle = Math.atan((py - y)/(px - x));
	if (px < x) {
	    angle += Math.PI;
	}

	dx = Math.cos(angle) * speed;
	dy = Math.sin(angle) * speed;
	
	if (!ready) {
	    if (x > r && x < GamePanel.WIDTH - r &&
		y > r && y < GamePanel.HEIGHT - r) {
		ready = true;
	    }
	}

	if (x < r && dx < 0) {
	    dx = -dx;
	}
	if (y < r && dy < 0) {
	    dy = -dy;
	}
	if (x > GamePanel.WIDTH - r && dx > 0) {
	    dx = -dx;
	}
	if (y > GamePanel.HEIGHT - r && dy > 0) {
	    dy = -dy;
	}

	if (hit) {
	    long elapsed = (System.nanoTime() - hitTimer) / Conversion;
	    if (elapsed > 50) {
		hit = false;
		hitTimer = 0;
	    }
	}
    }

    public void draw(Graphics2D g) {

	if (hit) {
	    g.setColor(Color.WHITE);
	    g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
	
	    g.setStroke(new BasicStroke(3));
	    g.setColor(Color.WHITE.darker());
	    g.drawOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
	    g.setStroke(new BasicStroke(1));
	} else {
	    g.setColor(color1);
	    g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
	
	    g.setStroke(new BasicStroke(3));
	    g.setColor(color1.darker());
	    g.drawOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
	    g.setStroke(new BasicStroke(1));
	}
    }

}
