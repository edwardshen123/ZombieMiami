import java.awt.*;

public class Grenade {
    
    private double x;
    private double y;
    private int r;
    //explosive radius
    private int er;
    
    public final double initX;
    public final double initY;

    public double dx;
    public double dy;
    private double speed;

    public boolean atMaxRange;
    public final double maxRange;
    public final long explosionDelay;

    public long grenadeCountdownStartTime;

    public long grenadeReleaseStartTime;

    public boolean isFirstHolyHandGrenade;

    private Color color;

    public Grenade(double angleRad, double x, double y, boolean isHolyHandGrenade) {
	this.x = x;
	this.y = y;
	initX = x;
	initY = y;
	r = 2;
	er = 10;
	
	if (isHolyHandGrenade) {
	    grenadeReleaseStartTime = System.nanoTime();
	}

	isFirstHolyHandGrenade = false;

	speed = 5;
	dx = Math.cos(angleRad) * speed;
	dy = Math.sin(angleRad) * speed;
	color = Color.GREEN;
	atMaxRange = false;
	maxRange = 100.0;
	explosionDelay = 100;
    }

    public double getX() {return x;}
    public double getY() {return y;}
    public double getR() {return r;}
    public double getER() {return er;}
 
    public boolean update() {
	
	x += dx;
	y += dy;

	if (x < -r || x > GamePanel.WIDTH + r ||
	    y < -r || y > GamePanel.HEIGHT + r) {
	    return true;
	}
	return false;
    }

    public void draw(Graphics2D g) {
	g.setColor(color);
	g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
    }
}
