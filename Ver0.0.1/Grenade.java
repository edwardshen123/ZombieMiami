import java.awt.*;

public class Grenade {
    
    private double x;
    private double y;
    private double r;
    
    private double initX;
    private double initY;

    private double dx;
    private double dy;
    private double speed;

    private boolean atMaxRange;
    private double maxRange;
    public static final long explosionDelay;

    private Color color;
3
    public Grenade(double angleRad, double x, double y) {
	this.x = x;
	this.y = y;
	initX = x;
	initY = y;
	r = 2;
	
	speed = 5;
	dx = Math.cos(angleRad) * speed;
	dy = Math.sin(angleRad) * speed;
	color = Color.GREEN;
	atMaxRange = false;
	maxRange = 20.0;
	explosionDelay = 100;
    }

    public double getX() {return x;}
    public double getY() {return y;}
    public double getR() {return r;}

    public boolean update() {
	
	x += dx;
	y += dy;

	if (x < -r || x > GamePanel.WIDTH + r ||
	    y < -r || y > GamePanel.HEIGHT + r) {
	    return true;
	}
	return false;

	double delX = x - initX;
	double delY = y - initY;
	double dist = Math.sqrt(delX * delX + delY * delY);

	if (dist >= maxRange) {
	    dx = 0;
	    dy = 0;
	    atMaxRange = true;
	}
    }

    public void draw(Graphics2D g) {
	g.setColor(color);
	g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
    }
}
