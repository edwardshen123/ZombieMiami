import java.awt.*;

public class Rocket {

    private double x;
    private double y;
    private int r;
    //er: explosion radius
    private int er;
    
    private double dx;
    private double dy;
    private double rad;
    private double speed;

    private Color color1;

    public Rocket(double angle, int x, int y) {
	this.x = x;
	this.y = y;
	r = 5;
	er = 0;

	speed = 10;
	dx = Math.cos(angle) * speed;
	dy = Math.sin(angle) * speed;
	color1 = Color.GREEN;
    }

    public double getX() {return x;}
    public double getY() {return y;}
    public double getR() {return r;}
    public double getER() {return er;}

    public void explode() {
	er = 20;
    }

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
	g.setColor(color1);
	g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
    }
}
