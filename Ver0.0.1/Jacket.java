import java.util.Scanner;
import java.awt.*;

public class Jacket{

    private int x;
    private int y;
    private int r;

    private int dx;
    private int dy;
    private int speed;

    private boolean left;
    private boolean right;
    private boolean up;
    private boolean down;

    private Color color1;
    private Color color2;

    private int lives;
    private int attack;
    //public Mask m;
    //public Weapon w;

    public Jacket(){

	x = GamePanel.WIDTH / 2;
	y = GamePanel.HEIGHT / 2;
	r = 5;

	dx = 0;
	dy = 0;
	speed = 5;

	color1 = Color.WHITE;
	color2 = Color.RED;

	lives = 5;
	attack = 10;
	//m=selectMask();
    }
    /*
    public Mask selectMask() {
	System.out.println("Select Your Mask");
	Scanner sc = new Scanner();
	String sMask = sc.nextLine();
	Mask nMask;
	switch (sMask) {
	case "Rooster Mask":
	    nMask = new roosterMask();
	    break;
	default:
	    nMask = new roosterMask();
	    break;
	}
	return nMask;
    }
    */

    public void setLeft(boolean b) {
	left = b;
    }
    public void setRight(boolean b) {
	right = b;
    }
    public void setUp(boolean b) {
	up = b;
    }
    public void setDown(boolean b) {
	down = b;
    }

    public void update() {
	if (left) {
	    dx = -speed;
	}
	if (right) {
	    dx = speed;
	}
	if (up) {
	    dy = -speed;
	}
	if (down) {
	    dy = speed;
	}
	
	x += dx;
	y += dy;

	if (x < r) {
	    x = r;
	}
	if (y < r) {
	    y = r;
	}
	if (x > GamePanel.WIDTH - r) {
	    x = GamePanel.WIDTH - r;
	}
	if (y > GamePanel.HEIGHT - r) {
	    y = GamePanel.HEIGHT - r;
	}

	dx = 0;
	dy = 0;
    }

    public void draw(Graphics2D g) {
	g.setColor(color1);
	g.fillOval(x - r, y - r, 2 * r, 2 * r);

	g.setStroke(new BasicStroke(3));
	g.setColor(color1.darker());
	g.drawOval(x - r, y - r, 2 * r, 2 * r);
	g.setStroke(new BasicStroke(1));
    }
    public int getHealth(){
	return health;
    }

    public void setHealth(int newHealth){
	health=newHealth;;
    }

    public int getAttack(){
	return attack;
    }
    
    public void setAttack(int newAttack){
	attack=newAttack;
    }

    public int getSpeed(){
	return speed;
    }

    public void setSpeed(int newSpeed){
	speed=newSpeed;
    }

    public String toString(){
	return m.getName()+" Jacket";
    }

}
